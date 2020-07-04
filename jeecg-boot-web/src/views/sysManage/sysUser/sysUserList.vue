<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>用户管理：用于系统管理登陆用户的统一功能，支持添加用户、冻结用户、删除用户、修改密码等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.statusCode"
                            placeholder="请选择用户状态" style="width:140px">
                            <Option value="0">正常</Option>
                            <Option value="1">冻结</Option>
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
                        <Button type="primary" @click="onAddUser" v-auth="['sysUser:add']">添加用户</Button>
                    </td>
                    <td >
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchUser">
                            <Button v-auth="['sysUser:delete']" type="error" ghost :disabled="this.modelTable.select<=0">批量删除</Button>
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
                <vxe-table-column field="userName" title="用户账号"></vxe-table-column>
                <vxe-table-column field="realName" title="真实姓名"></vxe-table-column>
                <vxe-table-column field="avatar" title="用户头像" align="center">
                    <template v-slot="{ row }">
                        <Avatar v-if="row.avatar!=null&&row.avatar!=''" :src="fileViewServiceUrl+row.avatar" />
                        <Avatar v-else icon="ios-person" />
                    </template>
                </vxe-table-column>
                <vxe-table-column field="phone" title="手机号码" align="center"></vxe-table-column>
                <vxe-table-column field="email" title="邮箱"></vxe-table-column>
                <vxe-table-column field="statusCode" width="80" title="状态" align="center">
                    <template v-slot="{ row }">
                        <Tag v-if="row.statusCode=='0'" color="success">正常</Tag>
                        <Tag v-if="row.statusCode=='1'" color="warning">停用</Tag>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="action" title="操作" align="center">
                    <template v-slot="{ row }">
                        <a href="javascript:void(0)" @click="onEditUser(row)" v-auth="['sysUser:edit']">
                            编辑
                        </a>
                        <div class="split"></div>
                        <Dropdown transfer v-auth="['sysUser:updatePassword','sysUser:frozen','sysUser:delete']">
                            <a href="javascript:void(0)">
                                更多
                                <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                                <div @click="onChangePassword(row)" v-auth="['sysUser:updatePassword']">
                                    <DropdownItem>重设密码</DropdownItem>
                                </div>
                                <div  v-auth="['sysUser:frozen']">
                                    <Poptip v-if="row.statusCode=='0'" transfer confirm title="确定冻结吗？"
                                        @on-ok="onBatchUser(row)">
                                        <DropdownItem>冻结用户</DropdownItem>
                                    </Poptip>
                                    <Poptip v-if="row.statusCode=='1'" transfer confirm title="确定解冻吗？"
                                        @on-ok="onBatchUser(row)">
                                        <DropdownItem>解冻用户</DropdownItem>
                                    </Poptip>
                                </div>

                                <div v-auth="['sysUser:delete']">
                                    <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteUser(row)">
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
            <Page :page-size-opts="[10, 20, 30, 40]" :total="modelTable.paging.total"
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
        USER_LIST_SERVICE,
        USER_DELETE_SERVICE,
        USER_DELETE_BATCH_SERVICE,
        USER_BATCH_SERVICE,
        FILE_VIEW_SERVICE_URL
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';
    import Treeselect from '@riophae/vue-treeselect';
    import '@riophae/vue-treeselect/dist/vue-treeselect.css';
    import userSave from './sysUserSave'
    import userPassword from './sysUserPassword'
    export default {
        components: {
            tablePannel,
            Treeselect,
            userSave,
            userPassword
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
                },

                fileUploadHeaders: {},
                fileViewServiceUrl: FILE_VIEW_SERVICE_URL,

                currentComponent: "",
                currentComponentKey: ""

            }
        },
        mounted() {

            this.fileUploadHeaders = {
                "X-Access-Token": Vue.ls.get(ACCESS_TOKEN)
            };

            this.getUserList();
        },
        methods: {
            onSearch() {
                this.resetPage();
                this.getUserList();
            },
            getUserList() {

                var vm = this;

                this.modelTable.loading = true;

                USER_LIST_SERVICE(Object.assign(this.modelTable.filter, {

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
            onAddUser() {

                this.currentComponent = userSave;
                this.currentComponentKey = null;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加用户";

                this.modelForm.id = null;

            },
            onEditUser(user) {

                this.currentComponent = userSave;
                this.currentComponentKey = user.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "编辑用户（" + user.realName + "）";

                this.modelForm.id = user.id;

            },
            onChangePassword(user) {

                this.currentComponent = userPassword;
                this.currentComponentKey = user.id;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "重设密码（" + user.realName + "）";

                this.modelForm.id = user.id;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.currentComponent.name) {
                    case "userSave":
                        this.$refs.drawerComponent.onSaveUser();
                        break;
                    case "userPassword":
                        this.$refs.drawerComponent.onChangePassword();
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

                this.getUserList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onBatchUser(user) {

                var statusCode = user.statusCode == 0 ? 1 : 0;

                USER_BATCH_SERVICE({
                    ids: user.id,
                    statusCode: statusCode
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(user.statusCode == 0 ? '冻结成功' : '解冻成功');
                        this.getUserList();
                    }
                });
            },
            onDeleteUser(user) {
                USER_DELETE_SERVICE({
                    id: user.id,
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getUserList();
                    }
                });
            },
            onDeleteBatchUser() {

                var ids = [];
                var userList = this.$refs.modelTable.getCheckboxRecords();
                _.forEach(userList, function (user) {
                    ids.push(user.id)
                });

                USER_DELETE_BATCH_SERVICE({
                    ids: _.join([ids], ','),
                }).then(response => {
                    if (response.success) {
                        this.$Message.success('删除成功');
                        this.getUserList();
                    }
                });
            },
            onTableCheckChange() {
                this.modelTable.select = this.$refs.modelTable.getCheckboxRecords();
            },
            onPageChange(pageIndex) {
                this.modelTable.paging.pageIndex = pageIndex;
                this.getUserList();
            },
            onPageSizeChange(pageSize) {
                this.modelTable.paging.pageSize = pageSize;
                this.getUserList();
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