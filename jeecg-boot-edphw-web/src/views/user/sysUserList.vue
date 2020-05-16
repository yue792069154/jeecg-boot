<template>
    <table-pannel ref="tablePannel" @on-ok="onOk" @on-cancel="onCancel">
        <slot slot="alert">
            <Alert show-icon>用户管理，支持EXCEL导入、导出、添加用户、冻结用户、删除用户等功能</Alert>
        </slot>
        <slot slot="filter">
            <table>
                <tr>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.status" placeholder="请选择用户状态"
                            style="width:140px">
                            <Option value="1">正常</Option>
                            <Option value="2">冻结</Option>
                        </Select>
                    </td>
                    <td>
                        <Select clearable @on-change="onSearch" v-model="modelTable.filter.sex" placeholder="请选择用户性别"
                            style="width:140px">
                            <Option value="1">男性</Option>
                            <Option value="2">女性</Option>
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
                        <Button type="text" icon="ios-cloud-upload" @click="onExportUserXls">导出</Button>
                    </td>
                    <td>
                        <Upload :show-upload-list="false" :headers="fileUploadHeaders" :action="importServiceUrl"
                            :on-success="onImportUserXls" :format="['xls','xlsx']">
                            <Button type="text" icon="ios-cloud-download">导入</Button>
                        </Upload>
                    </td>
                    <td>
                        <Button type="primary" @click="onAddUser">添加用户</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchUser">
                            <Button type="error" ghost :disabled="this.selectedEntityList<=0">批量删除</Button>
                        </Poptip>
                    </td>

                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.total}} 条</slot>
        <slot slot="grid">
            <Table :loading="modelTable.loading" stripe :columns="modelTable.columns" :data="modelTable.data"
                @on-selection-change="getSelectedEntityList">

                <template slot-scope="{ row }" slot="status_dictText">
                    <Tag v-if="row.status==1" closable color="success" v-text="row.status_dictText"></Tag>
                    <Tag v-if="row.status==2" closable color="warning" v-text="row.status_dictText"></Tag>
                </template>

                <template slot-scope="{ row }" slot="avatar">
                    <Avatar v-if="row.avatar!=null&&row.avatar!=''" :src="fileViewServiceUrl+row.avatar" />
                    <Avatar v-else icon="ios-person" />
                </template>

                <template slot-scope="{ row }" slot="action">
                    <a href="javascript:void(0)" @click="onEditUser(row)">
                        编辑
                    </a>
                    <Divider type="vertical" />
                    <Dropdown transfer>
                        <a href="javascript:void(0)">
                            更多
                            <Icon type="ios-arrow-down"></Icon>
                        </a>
                        <DropdownMenu slot="list">
                            <div @click="onChangePassword(row)">
                                <DropdownItem>重设密码</DropdownItem>
                            </div>
                            <div>
                                <Poptip v-if="row.status==1" transfer confirm title="确定冻结吗？" @on-ok="onBatchUser(row)">
                                    <DropdownItem>冻结用户</DropdownItem>
                                </Poptip>
                                <Poptip v-if="row.status==2" transfer confirm title="确定解冻吗？" @on-ok="onBatchUser(row)">
                                    <DropdownItem>解冻用户</DropdownItem>
                                </Poptip>
                            </div>

                            <div>
                                <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteUser(row)">
                                    <DropdownItem>删除用户</DropdownItem>
                                </Poptip>
                            </div>

                        </DropdownMenu>
                    </Dropdown>
                </template>
            </Table>
        </slot>
        <slot slot="page">
            <Page :page-size-opts="[10, 20, 50, 100]" :total="modelTable.paging.total"
                :current="modelTable.paging.pageIndex" :page-size="modelTable.paging.pageSize" @on-change="onPageChange"
                @on-page-size-change="onPageSizeChange" show-total show-sizer show-elevator />
        </slot>
        <slot slot="drawer">
            <user-save v-show="ref=='userSave'" ref="userSave" @on-save-error="onSaveError"
                @on-save-success="onSaveSuccess" :id="modelForm.id">
            </user-save>
            <user-Password v-show="ref=='userPassword'" ref="userPassword" @on-save-error="onSaveError"
                @on-save-success="onSaveSuccess" :id="modelForm.id"></user-password>
        </slot>
    </table-pannel>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import tablePannel from '../../components/table-pannel';
    import {
        USER_LIST_SERVICE,
        USER_BATCH_SERVICE,
        USER_DELETE_SERVICE,
        DUPLICATE_CHECK_SERVICE,
        USER_ROLE_LIST_SERVICE,
        USER_DEPART_LIST_SERVICE,
        FILE_VIEW_SERVICE_URL,
        USER_DELETE_BATCH_SERVICE,
        USER_EXPORT_SERVICE_URL,
        USER_IMPORT_SERVICE_URL
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
    import userSave from './sysUserSave'
    import userPassword from './sysUserPassword'
    export default {
        components: {
            tablePannel,
            Treeselect,
            userSave,
            userPassword
        },
        mixins: [ExcelMixins],
        data() {
            return {

                modelForm: {
                    id: ""
                },
                modelTable: {
                    columns: [{
                            type: 'selection',
                            width: 60,
                            align: 'center'
                        }, {
                            title: '用户账号',
                            key: 'username',
                            ellipsis: true,
                            tooltip: true
                        },

                        {
                            title: '真实姓名',
                            key: 'realname',
                            ellipsis: true,
                            tooltip: true
                        },
                        {
                            title: '用户头像',
                            key: 'avatar',
                            align: "center",
                            ellipsis: true,
                            tooltip: true,
                            slot: 'avatar'
                        },
                        {
                            title: '性别',
                            key: 'sex_dictText',
                            align: "center",
                            ellipsis: true,
                            tooltip: true
                        },
                        {
                            title: '手机号码',
                            key: 'phone',
                            align: "center",
                            ellipsis: true,
                            tooltip: true
                        },
                        {
                            title: '邮箱',
                            key: 'email',
                            ellipsis: true,
                            tooltip: true
                        },
                        {
                            title: '状态',
                            key: 'status_dictText',
                            align: "center",
                            ellipsis: true,
                            tooltip: true,
                            slot: 'status_dictText'
                        },
                        {
                            title: '操作',
                            key: 'action',
                            align: "center",
                            ellipsis: true,
                            tooltip: true,
                            slot: 'action'
                        }
                    ],
                    data: [],
                    paging: {
                        pageSize: 10,
                        pageIndex: 1,
                        total: 0
                    },
                    filter: {
                        sex: "",
                        status: "",
                        keyword: "",
                        keywordRange: "username,realname,phone,work_no,email,telephone"
                    },
                    loading: false
                },

                selectedEntityList: [],

                fileUploadHeaders: {},
                fileViewServiceUrl: FILE_VIEW_SERVICE_URL,
                importServiceUrl: USER_IMPORT_SERVICE_URL,

                ref: ""

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

                this.modelTable.loading = true;

                USER_LIST_SERVICE(Object.assign(this.modelTable.filter, {

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
            onAddUser() {

                this.ref = "userSave";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加用户";


            },
            onEditUser(user) {

                this.ref = "userSave";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "编辑用户";
                this.modelForm.id = user.id;

            },
            onChangePassword(user) {

                this.ref = "userPassword";
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "重设密码";
                this.modelForm.id = user.id;

            },
            onOk() {

                this.$refs.tablePannel.showLoading = true;

                switch (this.ref) {
                    case "userSave":
                        this.$refs.userSave.onSaveUser();
                        break;
                    case "userPassword":
                        this.$refs.userPassword.onChangePassword();
                        break;
                    default:
                        break;
                };

            },
            onCancel() {

                this.$refs.userSave.resetFields();
                this.$refs.userPassword.resetFields();
                this.$refs.tablePannel.showDrawer = false;
                this.$refs.tablePannel.showLoading = false;
                this.modelForm.id = "";
                
            },
            onSaveSuccess() {

                this.getUserList();
                this.onCancel();

            },
            onSaveError() {
                this.$refs.tablePannel.showLoading = false;
            },
            onBatchUser(user) {
                //1 正常 2冻结
                var status = user.status == 1 ? 2 : 1;
                USER_BATCH_SERVICE({
                    ids: user.id,
                    status: status
                }).then(response => {
                    if (response.success) {
                        this.$Message.success(user.status == 1 ? '冻结成功' : '解冻成功');
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

                _.forEach(this.selectedEntityList, function (user) {
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
            onExportUserXls() {

                var ids = [];

                _.forEach(this.selectedEntityList, function (user) {
                    ids.push(user.id)
                });

                this.onExportXls({
                    fileName: "用户列表",
                    selectedEntityIdList: ids,
                    url: USER_EXPORT_SERVICE_URL
                });

            },
            onImportUserXls(response, file, fileList) {
                var vm = this;
                this.onImportXls(response, file, fileList, function () {
                    vm.getUserList();
                });
            },
            getSelectedEntityList(selection, row) {
                this.selectedEntityList = selection;
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
    @import "../../style/common.less";


    table {
        td {
            padding: 0px 5px;
        }
    }
</style>