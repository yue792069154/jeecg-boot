<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel" :show-ok-button="showOkButton">
        <slot slot="alert">
            <Alert show-icon>字典管理，支持EXCEL导入、导出、添加字典、删除字典等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select clearable @on-change="onSearch"  placeholder="请选择字典类型分组" v-model="modelTable.filter.dictTypeGroup" style="width:166px">
                            <Option v-for="item in dict.dictTypeGroup" :value="item.dictCode" :key="item.dictCode"
                                :label="item.dictName">
                            </Option>
                        </Select>
                    </td>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.statusCode"
                            placeholder="请选择字典类型状态" style="width:166px">
                            <Option value="0">正常</Option>
                            <Option value="1">停用</Option>
                        </Select>
                    </td>
                    <td>
                        <Divider type="vertical" />
                    </td>
                    <td>
                        <Input suffix="ios-search" @on-change="onSearch" v-model="modelTable.filter.keyword"
                            placeholder="请输入关键字查询" style="width: auto" />
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="action">

            <table>
                <tr>
                    <td>
                        <Button type="primary" @click="onAddDictType">添加字典类型</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchDictType">
                            <Button type="error" ghost :disabled="this.modelTable.select<=0">批量删除</Button>
                        </Poptip>
                    </td>

                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">
            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data"
                @checkbox-change="onTableCheckChange">
                <vxe-table-column type="checkbox" width="60" align="center"></vxe-table-column>
                <vxe-table-column field="dictTypeName" title="字典类型名称"></vxe-table-column>
                <vxe-table-column field="dictTypeCode" title="字典类型编码"></vxe-table-column>
                <vxe-table-column field="extendTableName" title="扩展表名称"></vxe-table-column>
                <vxe-table-column field="extendTableChineseName" title="扩展表中文名"></vxe-table-column>
                <vxe-table-column field="statusCode" width="80" title="状态" align="center">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="sort" title="顺序" align="right"></vxe-table-column>
                <vxe-table-column field="action" width="240" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditDictType(row)">
                            编辑
                        </a>
                        <Divider type="vertical" />
                        <a href="javascript:void(0)" @click="onEditDict(row)">
                            字典配置
                        </a>
                        <Divider type="vertical" />
                        <Dropdown transfer>
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                                <div>
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteDictType(row)">
                                        <DropdownItem>删除字典类型</DropdownItem>
                                    </Poptip>
                                </div>
                                <div>
                                    <Poptip v-if="row.statusCode=='0'" transfer confirm title="确定停用吗？"
                                        @on-ok="onBatchDictType(row)">
                                        <DropdownItem>停用字典类型</DropdownItem>
                                    </Poptip>
                                    <Poptip v-if="row.statusCode=='1'" transfer confirm title="确定启用吗？"
                                        @on-ok="onBatchDictType(row)">
                                        <DropdownItem>启用字典类型</DropdownItem>
                                    </Poptip>
                                </div>
                            </DropdownMenu>
                        </Dropdown>
                    </template>
                </vxe-table-column>
            </vxe-table>
        </slot>
        <slot slot="page">
            <Page :page-size-opts="[10, 20, 50, 100]" :total="modelTable.paging.total"
                :current="modelTable.paging.pageIndex" :page-size="modelTable.paging.pageSize" @on-change="onPageChange"
                @on-page-size-change="onPageSizeChange" show-total show-sizer show-elevator />
        </slot>
        <slot slot="drawer">
            <component ref="drawerComponent" :key="currentComponentKey" :is="currentComponent"
                @on-save-error="onSaveError" @on-save-success="onSaveSuccess" :id="modelForm.id"></component>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        DICT_TYPT_LIST_SERVICE,
        DICT_TYPT_DELETE_SERVICE,
        DICT_TYPT_DELETE_BATCH_SERVICE,
        DICT_TYPT_BATCH_SERVICE,
        DICT_LIST_BY_DICT_TYPE_CODE_SERVICE
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import dictTypeSave from './sysDictTypeSave'
    import dictList from './sysDictList';
    export default {
        components: {
            tablePannel,
            dictTypeSave,
            dictList
        },
        data() {
            return {

                modelForm: {
                    id: ""
                },
                modelTable: {
                    data: [],
                    paging: {
                        pageSize: 10,
                        pageIndex: 1,
                        total: 0
                    },
                    select: [],
                    filter: {
                        dictTypeGroup:"",
                        statusCode: "",
                        keyword: ""
                    },
                    loading: false
                },
                dict: {
                    dictTypeGroup: []
                },

                currentComponent: "",
                currentComponentKey: "",

                showOkButton: false

            }
        },
        mounted() {
             this.getDict();
            this.getDictTypeList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getDictTypeList();
            },
            getDictTypeList() {

                var vm = this;

                this.modelTable.loading = true;

                DICT_TYPT_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: vm.modelTable.paging.pageIndex,
                    pageSize: vm.modelTable.paging.pageSize

                })).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        vm.modelTable.data = result.records || [];
                        vm.modelTable.paging.total = result.total;
                    };

                    vm.modelTable.loading = false;

                });

            },
            getDict() {

                var vm = this;

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "dictTypeGroup"
                }).then(response => {
                    if (response.success) {
                        vm.dict.dictTypeGroup = response.result;
                    };
                });


            },
            onAddDictType() {

                this.showOkButton = true;

                this.currentComponent = dictTypeSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加字典类型";

                this.modelForm.id = null;

            },
            onEditDictType(dictType) {

                this.showOkButton = true;

                this.currentComponent = dictTypeSave;
                this.currentComponentKey = dictType.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "编辑字典类型（" + dictType.dictTypeName + "）";

                this.modelForm.id = dictType.id;

            },
            onEditDict(dictType) {

                this.showOkButton = false;

                this.currentComponent = dictList;
                this.currentComponentKey = dictType.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "字典配置（" + dictType.dictTypeName + "）";

                this.modelForm.id = dictType.dictTypeCode;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;
                this.$refs.drawerComponent.onSaveDictType();

            },
            onCancel() {

                this.currentComponent = null;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;

                this.modelForm.id = null;

            },
            onSaveSuccess() {

                this.getDictTypeList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onBatchDictType(dictType) {

                var statusCode = dictType.statusCode == 0 ? 1 : 0;

                DICT_TYPT_BATCH_SERVICE({
                    ids: dictType.id,
                    statusCode: statusCode
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(dictType.statusCode == 0 ? '停用成功' : '启用成功');
                        this.getDictTypeList();
                    }
                });
            },
            onDeleteDictType(dictType) {
                DICT_TYPT_DELETE_SERVICE({
                    id: dictType.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getDictTypeList();
                    }
                });
            },
            onDeleteBatchDictType() {

                var ids = [];
                var dictTypeList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(dictTypeList, function (dictType) {
                    ids.push(dictType.id)
                });

                DICT_TYPT_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getDictTypeList();
                    }
                });
            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getDictTypeList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getDictTypeList();
            },
            resetPage: function () {
                this.modelTable.paging.pageIndex = 1;
                this.modelTable.paging.pageSize = 10;
            }

        }
    };
</script>
<style lang="less" scoped>
    @import "../../style/common.less";


    table {
        td {
            padding: 0px 5px;
        }
    }
</style>