<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>字典管理，支持EXCEL导入、导出、添加字典、删除字典等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Input suffix="ios-search" @on-change="onSearch" v-model="modelTable.filter.dictTypeName"
                            placeholder="请输入关键字查询" style="width: auto" />
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="action">

            <table>
                <tr>
                    <td>
                        <Button type="text" icon="ios-cloud-upload" @click="onExportUserXls">导出</Button>
                    </td>
                    <td>
                        <Upload :show-upload-list="false" :headers="fileUploadHeaders" :action="importServiceUrl"
                            :on-success="onImportUserXls" :format="['xls','xlsx']">
                            <Button type="text" icon="ios-cloud-download">导入</Button>
                        </Upload>
                    </td>
                    <td>
                        <Button type="primary" @click="onAddDictType">添加字典</Button>
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
                        <Tag v-if="row.statusCode==0" color="success">正常</Tag>
                        <Tag v-if="row.statusCode==-1" color="warning">停用</Tag>
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
                                        <DropdownItem>删除字典</DropdownItem>
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
            <dict-Type-save v-show="ref=='dictTypeSave'" ref="dictTypeSave" @on-save-error="onSaveError"
                @on-save-success="onSaveSuccess" :id="modelForm.id">
            </dict-Type-save>
            <dict-list v-show="ref=='dictList'" ref="dictList" :id="modelForm.id"></dict-list>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        DICT_TYPT_LIST_SERVICE,
        DICT_TYPT_BATCH_SERVICE,
        DICT_TYPT_DELETE_SERVICE,
        DICT_TYPT_DELETE_BATCH_SERVICE,
        DICT_TYPT_EXPORT_SERVICE_URL,
        DICT_TYPT_IMPORT_SERVICE_URL,
        DUPLICATE_CHECK_SERVICE
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../store/mutations';
    import {
        ExcelMixins
    } from '../../mixins/mixins';
    import dictTypeSave from './sysDictTypeSave'
    import dictList from './sysDictList';
    export default {
        components: {
            tablePannel,
            dictTypeSave,
            dictList
        },
        mixins: [ExcelMixins],
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
                        dictTypeName: ""
                    },
                    loading: false
                },

                fileUploadHeaders: {},
                importServiceUrl: DICT_TYPT_IMPORT_SERVICE_URL,

                ref: ""

            }
        },
        mounted() {

            this.fileUploadHeaders = {
                "X-Access-Token": Vue.ls.get(ACCESS_TOKEN)
            };

            this.getDictTypeList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getDictTypeList();
            },
            getDictTypeList() {

                this.modelTable.loading = true;

                DICT_TYPT_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: this.modelTable.paging.pageIndex,
                    pageSize: this.modelTable.paging.pageSize

                })).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        this.modelTable.data = result.records || [];
                        this.modelTable.paging.pageIndex = result.current;
                        this.modelTable.paging.pageSize = result.size;
                        this.modelTable.paging.total = result.total;
                    };

                    this.modelTable.loading = false;

                });

            },
            onAddDictType() {

                this.ref = "dictTypeSave";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加字典";

            },
            onEditDictType(dictType) {

                this.ref = "dictTypeSave";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "编辑字典";
                this.modelForm.id = dictType.id;

            },
            onEditDict(dictType) {

                this.ref = "dictList";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "字典配置（" + dictType.dictTypeName + "）";
                this.modelForm.id = dictType.dictTypeCode;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.ref) {
                    case "dictTypeSave":
                        this.$refs.dictTypeSave.onSaveDictType();
                        break;
                    default:
                        break;
                };

            },
            onCancel() {

                this.$refs.dictTypeSave.resetFields();
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
            onExportUserXls() {

                var ids = [];
                var dictTypeList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(dictTypeList, function (dictType) {
                    ids.push(dictType.id)
                });

                this.onExportXls({
                    fileName: "字典类型列表",
                    ids: ids,
                    url: DICT_TYPT_EXPORT_SERVICE_URL
                });

            },
            onImportUserXls(response, file, fileList) {
                var vm = this;
                this.onImportXls(response, file, fileList, function () {
                    vm.getDictTypeList();
                });
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getDictTypeList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getDictTypeList();
            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
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