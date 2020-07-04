<template>
    <table-pannel ref="tablePannel" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>字典管理：支持添加字典、删除字典、停用启用字典等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.statusCode"
                            placeholder="请选择字典状态" style="width:140px">
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
                        <Button type="primary" @click="onAddDict" v-auth="['sysDict:add']">添加字典</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchDict">
                            <Button type="error" v-auth="['sysDict:delete']" ghost
                                :disabled="this.modelTable.select<=0">批量删除</Button>
                        </Poptip>
                    </td>

                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">
            <vxe-table ref="modelTable" border stripe resizable highlight-current-row show-header-overflow show-overflow
                highlight-hover-row :loading="modelTable.loading" :data="modelTable.data"
                @checkbox-change="onTableCheckChange"
                :edit-config="{key: 'id', trigger: 'manual', mode: 'row', showStatus: true}">
                <vxe-table-column type="checkbox" width="60" align="center"></vxe-table-column>
                <vxe-table-column field="dictName" width="120" title="字典名称" prop="dictName"
                    :edit-render="{name: 'input', autoselect: true}">
                </vxe-table-column>
                <vxe-table-column field="dictCode" width="107" title="字典编码" prop="dictCode"
                    :edit-render="{name: 'input', autoselect: true}">
                </vxe-table-column>
                <vxe-table-column field="sort" width="71" title="顺序" align="right" prop="sort"
                    :edit-render="{name: '$input', autoselect: true,props: {type: 'number'}}">
                </vxe-table-column>
                <vxe-table-column field="statusCode" width="80" title="状态" align="center">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="action" width="130" title="操作" align="center">
                    <template v-slot="{ row }">

                        <template v-if="$refs.modelTable.isActiveByRow(row)">
                            <a href="javascript:void(0)" @click="onSaveDict(row)">
                                保存
                            </a>
                             <div class="split"></div>
                            <a href="javascript:void(0)" @click="$refs.modelTable.clearActived()">
                                取消
                            </a>
                        </template>
                        <template v-else>
                            <a href="javascript:void(0)" @click="onEditDict(row)" v-auth="['sysDict:edit']">
                                编辑
                            </a>
                            <div class="split"></div>
                            <Dropdown transfer v-auth="['sysDict:frozen','sysDict:delete']">
                                <a href="javascript:void(0)">
                                    更多
                                    <Icon type="ios-arrow-down"></Icon>
                                </a>
                                <DropdownMenu slot="list">
                                    <div v-auth="['sysDict:delete']">
                                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteDict(row)">
                                            <DropdownItem>删除字典</DropdownItem>
                                        </Poptip>
                                    </div>
                                    <div v-auth="['sysDict:frozen']">
                                        <Poptip v-if="row.statusCode=='0'" transfer confirm title="确定停用吗？"
                                            @on-ok="onBatchDict(row)">
                                            <DropdownItem>停用字典</DropdownItem>
                                        </Poptip>
                                        <Poptip v-if="row.statusCode=='1'" transfer confirm title="确定启用吗？"
                                            @on-ok="onBatchDict(row)">
                                            <DropdownItem>启用字典</DropdownItem>
                                        </Poptip>
                                    </div>

                                </DropdownMenu>
                            </Dropdown>
                        </template>
                    </template>
                </vxe-table-column>
            </vxe-table>
        </slot>
        <slot slot="page">
            <Page :page-size-opts="[10, 20, 50, 100]" :total="modelTable.paging.total"
                :current="modelTable.paging.pageIndex" :page-size="modelTable.paging.pageSize" @on-change="onPageChange"
                @on-page-size-change="onPageSizeChange" show-total show-sizer show-elevator />
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../../components/table-pannel';
    import {
        DICT_LIST_SERVICE,
        DICT_ADD_SERVICE,
        DICT_DELETE_SERVICE,
        DICT_DELETE_BATCH_SERVICE,
        DICT_EDIT_SERVICE,
        DICT_BATCH_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import ResultStatusEnum from '../../../libs/fw-core/fw-constant/constant';

    export default {
        name: "dictList",
        components: {
            tablePannel
        },
        props: {
            id: {
                type: String
            },
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
                        statusCode: "",
                        keyword: ""
                    },
                    loading: false
                }
            }
        },
        mounted() {
            this.getDictList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getDictList();
            },
            getDictList() {

                this.modelTable.loading = true;
                this.modelTable.filter.dictTypeCode = this.id;

                DICT_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: this.modelTable.paging.pageIndex,
                    pageSize: this.modelTable.paging.pageSize

                })).then(response => {

                    var result = response.result;


                    // if (resultData.status == ResultStatusEnum.SUCCESS && ObjectUtils.hasValue(resultData.data)) {

                    //         }
                    // if(response.code==ResultStatusEnum.SUCCESS&&)



                    if (!_.isNil(result)) {
                        this.modelTable.data = result.records || [];
                        this.modelTable.paging.total = result.total;
                    };

                    this.modelTable.loading = false;

                });

            },
            async onAddDict(row) {

                let record = {
                    dictName: '1'
                }

                let {
                    row: newRow
                } = await this.$refs.modelTable.insert(row)
                await this.$refs.modelTable.setActiveCell(newRow, 'dictName')

            },
            onEditDict(row) {

                this.$refs.modelTable.setActiveRow(row);

            },
            onSaveDict(row) {


                if (!row.id) {

                    row.dictTypeCode = this.id;

                    DICT_ADD_SERVICE(row).then(response => {
                        if (response.success) {
                            this.$Message.success('添加成功');
                        } else {
                            this.$Message.error(response.message);
                        }
                    }).then(response => {
                        this.getDictList();
                    });

                } else {

                    DICT_EDIT_SERVICE(row).then(response => {
                        if (response.success) {
                            this.$Message.success('编辑成功');
                        }
                    }).then(response => {
                        this.getDictList();
                    });

                }

                this.$refs.modelTable.clearActived();

            },
            onDeleteDict(dict) {
                DICT_DELETE_SERVICE({
                    id: dict.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getDictList();
                    }
                });
            },
            onDeleteBatchDict() {

                var ids = [];
                var dictList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(dictList, function (dict) {
                    ids.push(dict.id)
                });

                DICT_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getDictList();
                    }
                });
            },
            onBatchDict(dict) {
                var statusCode = dict.statusCode == 0 ? 1 : 0;

                DICT_BATCH_SERVICE({
                    ids: dict.id,
                    statusCode: statusCode
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(dict.statusCode == 0 ? '停用成功' : '启用成功');
                        this.getDictList();
                    }
                });
            },
            onCancel() {
                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;
            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getDictList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getDictList();
            },
            resetPage: function () {
                this.modelTable.paging.pageIndex = 1;
                this.modelTable.paging.pageSize = 10;
            }

        }
    };
</script>
<style lang="less" scoped>
    @import "../../../style/common.less";


    table {
        td {
            padding: 0px 5px;
        }
    }
</style>