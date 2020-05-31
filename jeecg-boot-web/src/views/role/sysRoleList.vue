<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>角色管理，支持EXCEL导入、导出、菜单授权等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Input suffix="ios-search" @on-change="onSearch" v-model="modelTable.filter.roleName"
                            placeholder="请输入关键字查询" style="width: auto" />
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="action">

            <table>
                <tr>
                    <td>
                        <Button type="text" icon="ios-cloud-upload" @click="onExportRoleXls">导出</Button>
                    </td>
                    <td>
                        <Upload :show-upload-list="false" :headers="fileUploadHeaders" :action="importServiceUrl"
                            :on-success="onImportRoleXls" :format="['xls','xlsx']">
                            <Button type="text" icon="ios-cloud-download">导入</Button>
                        </Upload>
                    </td>
                    <td>
                        <Button type="primary" @click="onAddRole">添加角色</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchRole">
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
                <vxe-table-column field="roleName" title="角色名称"></vxe-table-column>
                <vxe-table-column field="roleCode" title="角色编码"></vxe-table-column>
                <vxe-table-column field="action" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditRole(row)">
                            编辑
                        </a>
                        <Divider type="vertical" />
                        <Dropdown transfer>
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>

                            <DropdownMenu slot="list">
                                <div @click="onAuthUser(row)">
                                    <DropdownItem>授权用户</DropdownItem>
                                </div>
                            </DropdownMenu>
                            <DropdownMenu slot="list">
                                <div @click="onAuthMenu(row)">
                                    <DropdownItem>授权菜单</DropdownItem>
                                </div>
                            </DropdownMenu>
                            <DropdownMenu slot="list">
                                <div>
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteRole(row)">
                                        <DropdownItem>删除用户</DropdownItem>
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
            <role-save v-show="ref=='roleSave'" ref="roleSave" @on-save-error="onSaveError"
                @on-save-success="onSaveSuccess" :id="modelForm.id">
            </role-save>
            <role-user-list v-show="ref=='roleUserList'" ref="roleUserList" :id="modelForm.id"></role-user-list>
            <role-menu-list v-show="ref=='roleMenuList'" ref="roleMenuList" :id="modelForm.id"
                @on-save-error="onSaveError" @on-save-success="onSaveSuccess"></role-menu-list>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        ROLE_LIST_SERVICE,
        ROLE_ADD_SERVICE,
        ROLE_EDIT_SERVICE,
        ROLE_DELETE_SERVICE,
        ROLE_DELETE_BATCH_SERVICE,
        ROLE_EXPORT_SERVICE_URL,
        ROLE_IMPORT_SERVICE_URL
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
    import Treeselect from '@riophae/vue-treeselect';
    import '@riophae/vue-treeselect/dist/vue-treeselect.css';
    import roleSave from './sysRoleSave';
    import roleUserList from './sysRoleUserList';
    import roleMenuList from './sysRoleMenuList';
    export default {
        components: {
            tablePannel,
            Treeselect,
            roleSave,
            roleUserList,
            roleMenuList
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
                        roleName: ""
                    },
                    loading: false
                },

                fileUploadHeaders: {},
                importServiceUrl: ROLE_IMPORT_SERVICE_URL,

                ref: ""

            }
        },
        mounted() {

            this.fileUploadHeaders = {
                "X-Access-Token": Vue.ls.get(ACCESS_TOKEN)
            };

            this.getRoleList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getRoleList();
            },
            getRoleList() {

                this.modelTable.loading = true;

                ROLE_LIST_SERVICE(Object.assign(this.modelTable.filter, {

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
            onAddRole() {

                this.ref = "roleSave";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加角色";
                this.$refs.tablePannel.showOkButton = true;


            },
            onEditRole(role) {

                this.ref = "roleSave";
                this.modelForm.id = role.id;
                this.$refs.tablePannel.drawerTitle = "编辑角色";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.showOkButton = true;


            },
            onAuthUser(role) {

                this.ref = "roleUserList";
                this.modelForm.id = role.id;
                this.$refs.tablePannel.drawerTitle = "授权用户";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.showOkButton = false;

            },
            onAuthMenu(role) {

                this.ref = "roleMenuList";
                this.modelForm.id = role.id;
                this.$refs.tablePannel.drawerTitle = "授权菜单";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.showOkButton = true;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.ref) {
                    case "roleSave":
                        this.$refs.roleSave.onSaveRole();
                        break;
                    case "roleMenuList":
                        this.$refs.roleMenuList.onSaveAuthMenu();
                        break;
                    default:
                        break;
                };

            },
            onCancel() {

                this.$refs.roleSave.resetFields();
                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;
                this.modelForm.id = null;

            },
            onSaveSuccess() {

                this.getRoleList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onDeleteRole(role) {
                ROLE_DELETE_SERVICE({
                    id: role.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getRoleList();
                    }
                });
            },
            onDeleteBatchRole() {

                var ids = [];
                var roleList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(roleList, function (role) {
                    ids.push(role.id)
                });

                ROLE_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getRoleList();
                    }
                });
            },
            onExportRoleXls() {

                var ids = [];
                var roleList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(roleList, function (role) {
                    ids.push(role.id)
                });

                this.onExportXls({
                    fileName: "角色列表",
                    ids: ids,
                    url: ROLE_EXPORT_SERVICE_URL
                });

            },
            onImportRoleXls(response, file, fileList) {
                var vm = this;
                this.onImportXls(response, file, fileList, function () {
                    vm.getRoleList();
                });
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getRoleList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getRoleList();
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