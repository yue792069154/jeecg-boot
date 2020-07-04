<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel" :show-ok-button="showOkButton">
        <slot slot="alert">
            <Alert show-icon>功能管理：用于控制不同角色的按钮级别权限</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
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
                        <Button type="primary" v-auth="['sysAuth:add']" @click="onAddAuth">添加功能</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchAuth">
                            <Button type="error" v-auth="['sysAuth:delete']" ghost :disabled="this.modelTable.select<=0">批量删除</Button>
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
                <vxe-table-column field="authName" title="功能名称"></vxe-table-column>
                <vxe-table-column field="authCode" title="功能编码"></vxe-table-column>
                <vxe-table-column field="statusCode" title="状态" align="center" width="120">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="action" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditAuth(row)" v-auth="['sysAuth:edit']">
                            编辑
                        </a>
                         <div class="split"></div>
                        <Dropdown transfer v-auth="['sysAuth:frozen','sysAuth:delete']">
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                                
                                 <div v-auth="['sysAuth:frozen']">
                                    <Poptip v-if="row.statusCode=='0'" transfer confirm title="确定停用吗？"
                                        @on-ok="onBatchAuth(row)">
                                        <DropdownItem>停用功能</DropdownItem>
                                    </Poptip>
                                    <Poptip v-if="row.statusCode=='1'" transfer confirm title="确定启用吗？"
                                        @on-ok="onBatchAuth(row)">
                                        <DropdownItem>启用功能</DropdownItem>
                                    </Poptip>
                                </div>
                                <div v-auth="['sysAuth:delete']">
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteAuth(row)">
                                        <DropdownItem>删除功能</DropdownItem>
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
    import tablePannel from '../../../components/table-pannel';
    import {
        AUTH_LIST_SERVICE,
        AUTH_ADD_SERVICE,
        AUTH_EDIT_SERVICE,
        AUTH_DELETE_SERVICE,
        AUTH_DELETE_BATCH_SERVICE,
        AUTH_BATCH_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';

    import authSave from './sysAuthSave';
    export default {
        components: {
            tablePannel,
            authSave
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
                        keyword: ""
                    },
                    loading: false
                },

                currentComponent: "",
                currentComponentKey: "",

                showOkButton: false

            }
        },
        mounted() {
            this.onSearch();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getAuthList();
            },
            getAuthList() {

                var vm = this;

                this.modelTable.loading = true;

                AUTH_LIST_SERVICE(Object.assign(this.modelTable.filter, {

                    pageNo: this.modelTable.paging.pageIndex,
                    pageSize: this.modelTable.paging.pageSize

                })).then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        vm.modelTable.data = result.records || [];
                        vm.modelTable.paging.total = result.total;
                    };

                    vm.modelTable.loading = false;

                });

            },
            onAddAuth() {

                this.showOkButton = true;

                this.currentComponent = authSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加功能";
            },
            onEditAuth(auth) {

                this.showOkButton = true;

                this.currentComponent = authSave;
                this.currentComponentKey = auth.id;

                this.$refs.tablePannel.drawerTitle = "编辑功能（" + auth.authName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = auth.id;
            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.currentComponent.name) {
                    case "authSave":
                        this.$refs.drawerComponent.onSaveAuth();
                        break;
                    default:
                        break;
                };

            },
            onCancel() {

                this.currentComponent = null;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;

                this.modelForm.id = null;

            },
            onSaveSuccess() {

                this.getAuthList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onBatchAuth(auth) {

                var statusCode = auth.statusCode == 0 ? 1 : 0;

                AUTH_BATCH_SERVICE({
                    ids: auth.id,
                    statusCode: statusCode
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(auth.statusCode == 0 ? '停用成功' : '启用成功');
                        this.getAuthList();
                    }
                });
            },
            onDeleteAuth(auth) {
                AUTH_DELETE_SERVICE({
                    id: auth.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getAuthList();
                    }
                });
            },
            onDeleteBatchAuth() {

                var ids = [];
                var roleList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(roleList, function (auth) {
                    ids.push(auth.id)
                });

                AUTH_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getAuthList();
                    }
                });
            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getAuthList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getAuthList();
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