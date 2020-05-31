<template>
    <table-pannel ref="tablePannel" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>字典管理，支持EXCEL导入、导出、添加字典、删除字典等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Input suffix="ios-search" @on-change="onSearch" v-model="modelTable.filter.dictName"
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
                        <Button type="primary" @click="onAddDict">添加字典</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchDict">
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
                @checkbox-change="onTableCheckChange"
                :edit-config="{key: 'id', trigger: 'manual', mode: 'row', showStatus: true}">
                <vxe-table-column type="checkbox" width="60" align="center"></vxe-table-column>
                <vxe-table-column field="dictName" width="190" title="字典名称" prop="dictname"
                    :edit-render="{name: 'input', autoselect: true}">
                </vxe-table-column>
                <vxe-table-column field="dictCode" width="107" title="字典编码" prop="dictcode"
                    :edit-render="{name: 'input', autoselect: true}">
                </vxe-table-column>
                <vxe-table-column field="statusCode" width="80" title="状态" align="center">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode==0" color="success">正常</Tag>
                        <Tag v-if="row.statusCode==-1" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="action" width="130" title="操作" align="center">
                    <template v-slot="{ row }">

                        <template v-if="$refs.modelTable.isActiveByRow(row)">
                            <a href="javascript:void(0)" @click="onSaveDict(row)">
                                保存
                            </a>
                            <Divider type="vertical" />
                            <a href="javascript:void(0)" @click="$refs.modelTable.clearActived()">
                                取消
                            </a>
                        </template>
                        <template v-else>
                            <a href="javascript:void(0)" @click="onEditDict(row)">
                                编辑
                            </a>
                            <Divider type="vertical" />
                            <Dropdown transfer>
                                <a href="javascript:void(0)">
                                    更多
                                    <Icon type="ios-arrow-down"></Icon>
                                </a>
                                <DropdownMenu slot="list">
                                    <div>
                                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteDict(row)">
                                            <DropdownItem>删除字典</DropdownItem>
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
    import tablePannel from '../../components/table-pannel';
    import {
        DICT_LIST_SERVICE,
        DICT_ALL_LIST_SERVICE,
        DICT_BATCH_SERVICE,
        DICT_DELETE_SERVICE,
        DICT_DELETE_BATCH_SERVICE,
        DICT_EXPORT_SERVICE_URL,
        DICT_IMPORT_SERVICE_URL,
        DUPLICATE_CHECK_SERVICE,
        DICT_EDIT_SERVICE,
        DICT_ADD_SERVICE,
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
    import dictSave from './sysDictSave'
    export default {
        components: {
            tablePannel,
            dictSave
        },
        props: {
            id: {
                type: String
            },
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
                        pageSize: 50,
                        pageIndex: 1,
                        total: 0
                    },
                    select: [],
                    filter: {
                        dictName: ""
                    },
                    loading: false
                },

                fileUploadHeaders: {},
                importServiceUrl: DICT_IMPORT_SERVICE_URL,

                ref: ""

            }
        },
        watch: {
            id(newValue, oldValue) {
                this.onSearch();
            }
        },
        mounted() {

            this.fileUploadHeaders = {
                "X-Access-Token": Vue.ls.get(ACCESS_TOKEN)
            };

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

                    if (!_.isNil(result)) {
                        this.modelTable.data = result.records || [];
                        this.modelTable.paging.pageIndex = result.current;
                        this.modelTable.paging.pageSize = result.size;
                        this.modelTable.paging.total = result.total;
                    };

                    this.modelTable.loading = false;

                });

            },
            async onAddDict(row) {

                let record = {
                    dictname: '1'
                }

                let {
                    row: newRow
                } = await this.$refs.modelTable.insert(row)
                await this.$refs.modelTable.setActiveCell(newRow, 'dictname')

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
                    });

                } else {

                    DICT_EDIT_SERVICE(row).then(response => {
                        if (response.success) {
                            this.$Message.success('编辑成功');
                        }
                    });

                }
                this.onSearch();
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
            onCancel() {
                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;
            },
            onExportUserXls() {

                var ids = [];
                var dictList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(dictList, function (dict) {
                    ids.push(dict.id)
                });

                this.onExportXls({
                    fileName: "字典列表",
                    ids: ids,
                    url: DICT_EXPORT_SERVICE_URL
                });

            },
            onImportUserXls(response, file, fileList) {
                var vm = this;
                this.onImportXls(response, file, fileList, function () {
                    vm.getDictList();
                });
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getDictList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getDictList();
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