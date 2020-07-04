<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel" :show-ok-button="showOkButton">
        <slot slot="alert">
            <Alert show-icon>角色管理：支持授权用户、授权菜单、授权功能等功能</Alert>
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
                        <Button type="primary" @click="onAddRole" v-auth="['sysRole:add']">添加角色</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchRole">
                            <Button v-auth="['sysRole:delete']" type="error" ghost
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
                @checkbox-change="onTableCheckChange">
                <vxe-table-column type="checkbox" width="60" align="center"></vxe-table-column>
                <vxe-table-column field="roleName" title="角色名称"></vxe-table-column>
                <vxe-table-column field="roleCode" title="角色编码"></vxe-table-column>
                <vxe-table-column field="roleEntryProtContent" title="角色入口"></vxe-table-column>
                <vxe-table-column field="action" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditRole(row)" v-auth="['sysRole:edit']">
                            编辑
                        </a>
                        <div class="split"></div>
                        <Dropdown transfer
                            v-auth="['sysRole:authUser','sysRole:authMenu','sysRole:authAuth','sysRole:delete']">
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>

                            <DropdownMenu slot="list">
                                <div @click="onAuthUser(row)" v-auth="['sysRole:authUser']">
                                    <DropdownItem>授权用户</DropdownItem>
                                </div>
                            </DropdownMenu>
                            <DropdownMenu slot="list">
                                <div @click="onAuthMenu(row)" v-auth="['sysRole:authMenu']">
                                    <DropdownItem>授权菜单</DropdownItem>
                                </div>
                            </DropdownMenu>
                            <DropdownMenu slot="list" v-auth="['sysRole:authAuth']">
                                <div @click="onAuthAuth(row)">
                                    <DropdownItem>授权功能</DropdownItem>
                                </div>
                            </DropdownMenu>
                            <DropdownMenu slot="list" v-auth="['sysRole:delete']">
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
        ROLE_LIST_SERVICE,
        ROLE_ADD_SERVICE,
        ROLE_EDIT_SERVICE,
        ROLE_DELETE_SERVICE,
        ROLE_DELETE_BATCH_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';

    import roleSave from './sysRoleSave';
    import roleUserList from './sysRoleUserList';
    import roleMenuList from './sysRoleMenuList';
    import roleAuthList from './sysRoleAuthList';

    export default {
        components: {
            tablePannel,
            roleSave,
            roleUserList,
            roleMenuList,
            roleAuthList
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
                this.getRoleList();
            },
            getRoleList() {

                var vm = this;

                this.modelTable.loading = true;

                ROLE_LIST_SERVICE(Object.assign(this.modelTable.filter, {

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
            onAddRole() {

                this.showOkButton = true;

                this.currentComponent = roleSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加角色";
            },
            onEditRole(role) {

                this.showOkButton = true;

                this.currentComponent = roleSave;
                this.currentComponentKey = role.id;

                this.$refs.tablePannel.drawerTitle = "编辑角色（" + role.roleName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = role.id;
            },
            onAuthUser(role) {

                this.showOkButton = false;

                this.currentComponent = roleUserList;
                this.currentComponentKey = role.id;


                this.$refs.tablePannel.drawerTitle = "授权用户（" + role.roleName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = role.id;

            },
            onAuthMenu(role) {

                this.showOkButton = true;

                this.currentComponent = roleMenuList;
                this.currentComponentKey = role.id;

                this.$refs.tablePannel.drawerTitle = "授权菜单（" + role.roleName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = role.id;

            },
            onAuthAuth(role) {

                this.showOkButton = false;

                this.currentComponent = roleAuthList;
                this.currentComponentKey = role.id;

                this.$refs.tablePannel.drawerTitle = "授权功能（" + role.roleName + "）";
                this.$refs.tablePannel.showDrawer = true;

                this.modelForm.id = role.id;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.currentComponent.name) {
                    case "roleSave":
                        this.$refs.drawerComponent.onSaveRole();
                        break;
                    case "roleMenuList":
                        this.$refs.drawerComponent.onSaveAuthMenu();
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
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getRoleList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getRoleList();
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