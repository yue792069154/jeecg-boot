<template>
    <table-pannel ref="tablePannel" @on-ok="onSaveUser">
        <slot slot="alert">
            <Alert show-icon>用户管理，支持EXCEL导入、导出、添加用户、冻结用户、删除用户等功能</Alert>
        </slot>
        <slot slot="filter">2</slot>
        <slot slot="action">
            <table>
                <tr>
                    <td>
                        <Button type="primary" @click="onAddUser">添加用户</Button>
                    </td>
                    <td>
                        <Poptip transfer confirm title="确定删除吗？" @on-ok="onDeleteBatchUser">
                            <Button type="error" ghost :disabled="this.selectedEntityList<=0">批量删除</Button>
                        </Poptip>
                    </td>
                    <td>
                        <Button type="dashed" icon="ios-cloud-upload" @click="onExportUserXls">导出</Button>
                    </td>
                    <td>
                        <Upload :show-upload-list="false" :headers="fileUploadHeaders" :action="importServiceUrl"
                            :on-success="onImportUserXls" :format="['xls','xlsx']">
                            <Button type="dashed" icon="ios-cloud-download">导入</Button>
                        </Upload>
                    </td>
                </tr>
            </table>
        </slot>
        <slot slot="summary">总计：筛选符合条件记录：{{modelTable.paging.totalCount}} 条</slot>
        <slot slot="grid">
            <Table stripe :columns="modelTable.columns" :data="modelTable.data"
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
                    <a href="javascript:void(0)" @click="onViewUser(row)">
                        编辑
                    </a>
                    <Dropdown transfer @on-click="onDropdownMenuClick">
                        <a href="javascript:void(0)">
                            更多
                            <Icon type="ios-arrow-down"></Icon>
                        </a>
                        <DropdownMenu slot="list">
                            <div @click="onViewUser(row)">
                                <DropdownItem>详细信息</DropdownItem>
                            </div>
                            <DropdownItem name="onChangeUserPassword">重设密码</DropdownItem>
                            <DropdownItem divided>代理用户</DropdownItem>

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
            <Page :total="100" show-total show-sizer show-elevator />
        </slot>
        <slot slot="drawer">
            <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
                <FormItem label="用户账号" prop="username">
                    <Input placeholder="请输入用户账号" v-model="modelForm.username" clearable />
                </FormItem>
                <FormItem label="真实姓名" prop="realname">
                    <Input placeholder="请输入真实姓名" v-model="modelForm.realname" clearable />
                </FormItem>
                <FormItem label="登陆密码" prop="salt" v-if="!modelForm.id">
                    <Input placeholder="请输入登陆密码" v-model="modelForm.salt" clearable />
                </FormItem>
                <FormItem label="确认密码" prop="confirmSalt" v-if="!modelForm.id">
                    <Input placeholder="请再次输入登陆密码" v-model="modelForm.confirmSalt" clearable />
                </FormItem>
                <FormItem label="用户头像">
                    <div class="img-upload-view-pannel">
                        <Avatar size="large" v-if="modelForm.avatar!=null&&modelForm.avatar!=''"
                            :src="fileViewServiceUrl+modelForm.avatar" />
                        <Upload :show-upload-list="false" :on-success="onUploadSucess"
                            :format="['jpg','jpeg','png','bmp']" :max-size="2048" :on-format-error="onUploadFormatError"
                            :on-exceeded-size="onExceededSize" :action="fileUploadServiceUrl"
                            :headers="fileUploadHeaders">
                            <div class="img-upload-pannel">
                                <Icon type="ios-camera" size="30"></Icon>
                            </div>
                        </Upload>
                    </div>
                </FormItem>
                <FormItem label="用户工号" prop="workNo">
                    <Input placeholder="请输入用户工号" v-model="modelForm.workNo" clearable />
                </FormItem>
                <FormItem label="职务信息" prop="post">
                    <Select multiple placeholder="请选择职务信息" v-model="modelForm.post">
                        <Option v-for="item in positionList" :value="item.id" :key="item.id" :label="item.name">
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="用户角色" prop="selectedroles">
                    <Select multiple placeholder="请选择用户角色" v-model="modelForm.selectedroles">
                        <Option v-for="item in roleList" :value="item.id" :key="item.id" :label="item.roleName">
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="所属部门" prop="selecteddeparts">
                    <Input placeholder="请选择职务信息" v-model="modelForm.selecteddeparts" clearable search
                        enter-button="选择" />
                </FormItem>
                <FormItem label="出生年月" prop="birthday">
                    <DatePicker type="date" placeholder="请选择出生年月" v-model="modelForm.birthday" style="width: 100%">
                    </DatePicker>
                </FormItem>
                <FormItem label="用户性别" prop="sex">
                    <RadioGroup v-model="modelForm.sex">
                        <Radio label="1" border>男性</Radio>
                        <Radio label="2" border>女性</Radio>
                    </RadioGroup>
                </FormItem>
                <FormItem label="用户邮箱" prop="email">
                    <Input placeholder="请输入用户邮箱" v-model="modelForm.email" clearable />
                </FormItem>
                <FormItem label="手机号码" prop="phone">
                    <Input placeholder="请输入手机号码" v-model="modelForm.phone" clearable />
                </FormItem>
                <FormItem label="座机号码" prop="telephone">
                    <Input placeholder="请输入座机号码" v-model="modelForm.telephone" clearable />
                </FormItem>
            </Form>
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
        ROLE_ALL_LIST_SERVICE,
        POSITION_ALL_LIST_SERVICE,
        USER_EXCEL_EXPORT_SERVICE,
        USER_EXCEL_IMPORT_SERVICE,
        DUPLICATE_CHECK_SERVICE,
        USER_EDIT_SERVICE,
        USER_ROLE_LIST_SERVICE,
        USER_DEPART_LIST_SERVICE,
        USER_ADD_SERVICE,
        FILE_UPLOAD_SERVICE_URL,
        FILE_VIEW_SERVICE_URL,
        USER_DELETE_BATCH_SERVICE,
        USER_EXPORT_SERVICE_URL,
        USER_IMPORT_SERVICE_URL,
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

    export default {
        components: {
            tablePannel
        },
        mixins: [ExcelMixins],
        data() {
            return {
                modelForm: {
                    id: null,
                    birthday: null,
                    sex: null,
                    telephone: null,
                    avatar: null,
                    realname: null,
                    workNo: null,
                    post: null,
                    phone: null,
                    orgCode: null,
                    email: null,
                    username: null,
                    selectedroles: [],
                    selecteddeparts: [],
                    salt: null,
                    confirmSalt: null
                },
                modelFormRule: {
                    username: [{
                        required: true,
                        validator: this.validateUserName,
                        trigger: 'change,blur'
                    }],
                    salt: [{
                        required: true,
                        validator: this.validateToNextPassword,
                        trigger: 'change,blur'
                    }],
                    confirmSalt: [{
                        required: true,
                        validator: this.compareToFirstPassword,
                        trigger: 'change,blur'
                    }],
                    realname: [{
                        required: true,
                        message: '请输入真实姓名',
                        trigger: 'change,blur'
                    }],
                    phone: [{
                        validator: this.validatePhone,
                        trigger: 'change,blur'
                    }],
                    email: [{
                        validator: this.validateEmail,
                        trigger: 'change,blur'
                    }],
                    selectedroles: [{
                        required: true,
                        type: 'array',
                        message: '请选择用户角色',
                        trigger: 'change,blur'
                    }],
                    telephone: [{
                        pattern: /^0\d{2,3}-[1-9]\d{6,7}$/,
                        message: '请输入正确的座机号码',
                        trigger: 'change,blur'
                    }]
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
                            title: '生日',
                            key: 'birthday',
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
                        pageSize: 6,
                        pageIndex: 1,
                        totalCount: 0
                    }
                },
                roleList: [],
                positionList: [],

                selectedEntityList: [],

                fileUploadServiceUrl: FILE_UPLOAD_SERVICE_URL,
                fileUploadHeaders: "",
                fileViewServiceUrl: FILE_VIEW_SERVICE_URL,
                importServiceUrl: USER_IMPORT_SERVICE_URL

            }
        },
        mounted() {

            this.fileUploadHeaders = {
                "X-Access-Token": Vue.ls.get(ACCESS_TOKEN)
            };

            this.getPositionList();
            this.getRoleList();
            this.getUserList();
        },
        methods: {
            getUser() {
                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "详细信息";
            },
            getUserList() {
                USER_LIST_SERVICE().then(response => {

                    var result = response.result;

                    if (!_.isNil(result)) {
                        this.modelTable.data = result.records || [];
                        this.modelTable.paging.pageIndex = result.current;
                        this.modelTable.paging.totalCount = result.total;
                    };



                });

            },
            getPositionList() {
                POSITION_ALL_LIST_SERVICE().then(response => {

                    if (response.success) {
                        var result = response.result;
                        if (!_.isNil(result)) {
                            this.positionList = result;
                        };

                    }

                });

            },
            getRoleList() {

                ROLE_ALL_LIST_SERVICE().then(response => {

                    if (response.success) {
                        var result = response.result;
                        if (!_.isNil(result)) {
                            this.roleList = result;
                        };

                    }

                });


            },
            onDropdownMenuClick(name) {
                if (_.isFunction(this[name]))
                    this[name]();
            },
            onAddUser() {

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "添加用户";

                for (var item in this.modelForm) {
                    this.modelForm[item] = null;
                };

                this.$refs.modelForm.resetFields();

            },
            onViewUser(user) {

                var vm = this;

                this.$refs.tablePannel.showDrawer = true;
                this.$refs.tablePannel.drawerTitle = "编辑用户";

                for (var item in this.modelForm) {
                    this.modelForm[item] = user[item];
                };

                this.modelForm.sex = _.toString(this.modelForm.sex);
                this.modelForm.selectedroles = [];
                this.modelForm.selecteddeparts = [];

                USER_ROLE_LIST_SERVICE({
                    userid: user.id
                }).then(response => {
                    if (response.success) {
                        vm.modelForm.selectedroles = response.result;
                    };
                });

                USER_DEPART_LIST_SERVICE({
                    userId: user.id
                }).then(response => {
                    if (response.success) {
                        for (let i = 0; i < response.result.length; i++) {
                            vm.modelForm.selecteddeparts.push(response.result[i].key);
                        }
                    };
                });


            },
            onSaveUser() {

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        this.$refs.tablePannel.showDrawer = false;

                        this.modelForm.selectedroles = _.join([this.modelForm.selectedroles], ',');
                        this.modelForm.selecteddeparts = _.join([this.modelForm.selecteddeparts], ',');

                        if (_.isNil(this.modelForm.id)) {

                            USER_ADD_SERVICE(this.modelForm).then(response => {
                                if (response.success) {

                                    this.$Message.success('添加成功');
                                    this.getUserList();
                                }
                            });

                        } else {

                            USER_EDIT_SERVICE(this.modelForm).then(response => {
                                if (response.success) {

                                    this.$Message.success('编辑成功');
                                    this.getUserList();
                                }
                            });

                        }


                    };
                })

            },
            onChangeUserPassword() {

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
            onUploadSucess(response, file, fileList) {
                if (response.success) {
                    this.modelForm.avatar = response.message;
                };
            },
            onUploadFormatError(file) {
                this.$Notice.warning({
                    title: '上传文件格式错误',
                    desc: '上传的文件只能是JPG、JPEG、PNG、BMP格式'
                });
            },
            onExceededSize(file) {
                this.$Notice.warning({
                    title: '超过最大上传限制',
                    desc: '上传的文件过大, 不允许超过 2M'
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
            validateUserName(rule, value, callback) {

                if (_.isNil(value)) {
                    callback("请输入用户账号")
                } else {
                    DUPLICATE_CHECK_SERVICE({
                        tableName: 'sys_user',
                        fieldName: 'username',
                        fieldVal: value,
                        dataId: this.modelForm.id
                    }).then(response => {
                        if (response.success) {
                            callback()
                        } else {
                            callback("用户账号已存在")
                        }
                    });

                }


            },
            compareToFirstPassword(rule, value, callback) {

                if (_.isNil(value)) {
                    callback('请再次输入登陆密码');
                } else {
                    if (value !== this.modelForm.salt) {
                        callback('两次输入的密码不一致');
                    } else {
                        callback()
                    }
                };

            },
            validateToNextPassword(rule, value, callback) {

                if (_.isNil(value)) {
                    callback("请输入登陆密码")
                } else {
                    if (new RegExp(/(?=.*[a-z])(?=.*\d)(?=.*[#@!~%^&*])[a-z\d#@!~%^&*]{8,16}/i).test(value)) {
                        this.$refs.modelForm.validateField('confirmSalt');
                        callback()
                    } else {
                        callback("密码长度8-16位，必须包含数字、字母、特殊符号");
                    }
                }

            },
            validatePhone(rule, value, callback) {
                if (_.isNil(value)) {
                    callback()
                } else {
                    if (new RegExp(/^1[3|4|5|7|8|9][0-9]\d{8}$/).test(value)) {


                        DUPLICATE_CHECK_SERVICE({
                            tableName: 'sys_user',
                            fieldName: 'phone',
                            fieldVal: value,
                            dataId: this.modelForm.id
                        }).then(response => {
                            if (response.success) {
                                callback()
                            } else {
                                callback("手机号已存在")
                            }
                        });


                    } else {
                        callback("请输入正确格式的手机号码");
                    }
                }
            },
            validateEmail(rule, value, callback) {
                if (_.isNil(value)) {
                    callback()
                } else {
                    if (new RegExp(
                            /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                        ).test(value)) {


                        DUPLICATE_CHECK_SERVICE({
                            tableName: 'sys_user',
                            fieldName: 'email',
                            fieldVal: value,
                            dataId: this.modelForm.id
                        }).then(response => {
                            if (response.success) {
                                callback()
                            } else {
                                callback("邮箱已存在")
                            }
                        });

                    } else {
                        callback("请输入正确格式的邮箱")
                    }
                }
            }
        }
    };
</script>
<style lang="less" scoped>
    @import "../../style/common.less";
</style>