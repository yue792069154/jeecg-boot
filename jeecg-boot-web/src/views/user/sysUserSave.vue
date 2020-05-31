<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="用户账号" prop="userName">
            <Input placeholder="请输入用户账号" v-model="modelForm.userName" clearable />
        </FormItem>
        <FormItem label="真实姓名" prop="realName">
            <Input placeholder="请输入真实姓名" v-model="modelForm.realName" clearable />
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
                <Upload :show-upload-list="false" :on-success="onUploadSucess" :format="['jpg','jpeg','png','bmp']"
                    :max-size="2048" :on-format-error="onUploadFormatError" :on-exceeded-size="onExceededSize"
                    :action="fileUploadServiceUrl" :headers="fileUploadHeaders">
                    <div class="img-upload-pannel">
                        <Icon type="ios-camera" size="30"></Icon>
                    </div>
                </Upload>
            </div>
        </FormItem>
        <FormItem label="用户角色" prop="sysRoleIdList">
            <Select multiple placeholder="请选择用户角色" v-model="modelForm.sysRoleIdList">
                <Option v-for="item in roleList" :value="item.id" :key="item.id" :label="item.roleName">
                </Option>
            </Select>
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
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        POSITION_ALL_LIST_SERVICE,
        DUPLICATE_CHECK_SERVICE,
        USER_EDIT_SERVICE,
        USER_ROLE_ALL_LIST_SERVICE,
        USER_ADD_SERVICE,
        FILE_UPLOAD_SERVICE_URL,
        FILE_VIEW_SERVICE_URL,
        DEPART_TREE_LIST_SERVICE,
        ROLE_ALL_LIST_SERVICE,
        USER_QUERY_SERVICE
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../store/mutations';

    import Treeselect from '@riophae/vue-treeselect';
    import '@riophae/vue-treeselect/dist/vue-treeselect.css';

    export default {
        components: {
            Treeselect
        },
        props: {
            id: {
                type: String
            },
        },
        data() {
            return {

                modelForm: {
                    id: null,
                    sex: null,
                    telephone: null,
                    avatar: null,
                    realName: null,
                    phone: null,
                    orgCode: null,
                    email: null,
                    userName: null,
                    sysRoleIdList: [],
                    salt: null,
                    confirmSalt: null
                },
                modelFormRule: {
                    userName: [{
                        required: true,
                        //validator: this.validateUserName,
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
                    realName: [{
                        required: true,
                        message: '请输入真实姓名',
                        trigger: 'change,blur'
                    }],
                    phone: [{
                        //validator: this.validatePhone,
                        trigger: 'change,blur'
                    }],
                    email: [{
                        //validator: this.validateEmail,
                        trigger: 'change,blur'
                    }],
                    sysRoleIdList: [{
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

                roleList: [],

                fileUploadServiceUrl: FILE_UPLOAD_SERVICE_URL,
                fileUploadHeaders: {},
                fileViewServiceUrl: FILE_VIEW_SERVICE_URL

            }
        },
        watch: {
            id(newValue, oldValue) {
                this.getUser();

            }
        },
        mounted() {

            this.fileUploadHeaders = {
                "X-Access-Token": Vue.ls.get(ACCESS_TOKEN)
            };

            this.getRoleList();

        },
        methods: {
            getUser() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    USER_QUERY_SERVICE({
                        id: id
                    }).then(response => {

                        if (response.success) {
                            var result = response.result;
                            if (!_.isNil(result)) {
                                for (var item in vm.modelForm) {
                                    vm.modelForm[item] = result[item];
                                };

                                vm.modelForm.sex = _.toString(vm.modelForm.sex);
                                vm.modelForm.sysRoleIdList = vm.modelForm.sysRoleIdList || [];

                                USER_ROLE_ALL_LIST_SERVICE({
                                    userId: id
                                }).then(response => {
                                    if (response.success) {
                                        if (!_.isNil(response.result)) {
                                            _.forEach(response.result, function (item) {
                                                vm.modelForm.sysRoleIdList.push(item.roleId);
                                            });
                                        }
                                    };
                                });

                            };
                        }
                    });
                };
            },
            getRoleList() {

                ROLE_ALL_LIST_SERVICE({}).then(response => {

                    if (response.success) {
                        var result = response.result;
                        if (!_.isNil(result)) {
                            this.roleList = result;
                        };

                    }

                });


            },
            onSaveUser() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {


                        if (_.isNil(vm.modelForm.id)) {

                            USER_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            USER_EDIT_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('编辑成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        }




                    } else {
                        vm.$emit("on-save-error");
                    };
                });


            },
            resetFields() {
                this.$refs.modelForm.resetFields();
                this.modelForm.id = null;
                this.modelForm.avatar = null;
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
    .img-upload-pannel {
        width: 44px;
        height: 44px;
        line-height: 52px;
        border: 1px dashed #dcdee2;
        border-radius: 50%;
        text-align: center;
        cursor: pointer;
        margin-left: 5px;
    }

    .img-upload-view-pannel {
        display: flex;
        align-items: center;
    }
</style>