<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="用户账号" prop="userName">
            <Input placeholder="请输入用户账号" v-model="modelForm.userName" disabled />
        </FormItem>
        <FormItem label="真实姓名" prop="realName">
            <Input placeholder="请输入真实姓名" v-model="modelForm.realName" disabled />
        </FormItem>
        <FormItem label="登陆密码" prop="password">
            <Input placeholder="请输入登陆密码" v-model="modelForm.password" clearable />
        </FormItem>
        <FormItem label="确认密码" prop="confirmPassword">
            <Input placeholder="请再次输入登陆密码" v-model="modelForm.confirmPassword" clearable />
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        USER_CHANGE_PASSWORD_SERVICE,
        USER_QUERY_SERVICE
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../store/mutations';

    export default {
        name: "userPassword",
        components: {

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
                    userName: "",
                    realName: "",
                    password: null,
                    confirmPassword: null
                },
                modelFormRule: {
                    password: [{
                        required: true,
                        validator: this.validateToNextPassword,
                        trigger: 'blur'
                    }],
                    confirmPassword: [{
                        required: true,
                        validator: this.compareToFirstPassword,
                        trigger: 'blur'
                    }]
                }

            }
        },
        mounted() {
            this.getUser();
        },
        methods: {
            getUser(id) {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    USER_QUERY_SERVICE({
                        id: id
                    }).then(response => {

                        if (response.success) {
                            var result = response.result;
                            if (!_.isNil(result)) {
                                this.modelForm.id = result.id;
                                this.modelForm.userName = result.userName;
                                this.modelForm.realName = result.realName;
                            };
                        }
                    });
                };

            },
            onChangePassword() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        USER_CHANGE_PASSWORD_SERVICE(vm.modelForm).then(response => {
                            if (response.success) {

                                vm.$Message.success('密码修改成功');
                                vm.$emit("on-save-success");
                                vm.resetFields();

                            }
                        });

                    } else {
                        vm.$emit("on-save-error");
                    };
                });


            },
            compareToFirstPassword(rule, value, callback) {

                if (_.isNil(value)) {
                    callback('请再次输入登陆密码');
                } else {
                    if (value !== this.modelForm.password) {
                        callback('两次输入的密码不一致');
                    } else {
                        callback()
                    }
                };

            },
            resetFields() {
                this.$refs.modelForm.resetFields();
            },
            validateToNextPassword(rule, value, callback) {

                if (_.isNil(value)) {
                    callback("请输入登陆密码")
                } else {
                    if (new RegExp(/(?=.*[a-z])(?=.*\d)(?=.*[#@!~%^&*])[a-z\d#@!~%^&*]{8,16}/i).test(value)) {
                        this.$refs.modelForm.validateField('confirmPassword');
                        callback()
                    } else {
                        callback("密码长度8-16位，必须包含数字、字母、特殊符号");
                    }
                }

            }
        }
    };
</script>
<style lang="less" scoped>

</style>