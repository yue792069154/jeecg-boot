<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="功能名称" prop="authName">
            <Input placeholder="请输入功能名称" v-model="modelForm.authName" clearable />
        </FormItem>
        <FormItem label="功能编码" prop="authCode">
            <Input placeholder="请输入功能编码" v-model="modelForm.authCode" clearable />
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        AUTH_QUERY_SERVICE,
        AUTH_ADD_SERVICE,
        AUTH_EDIT_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';

    export default {
        name:"authSave",
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
                    authName: null,
                    authCode: null
                },
                modelFormRule: {
                    authName: [{
                        required: true,
                        message: '请输入功能名称',
                        trigger: 'change,blur'
                    }],
                    authCode: [{
                        required: true,
                        message: '请输入功能编码',
                        trigger: 'change,blur'
                    }]
                }

            }
        },
        mounted() {
            this.getRole();
        },
        methods: {
            getRole() {

                 var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    AUTH_QUERY_SERVICE({
                        id: id
                    }).then(response => {

                        if (response.success) {
                            var result = response.result;
                            if (!_.isNil(result)) {
                                for (var item in this.modelForm) {
                                    this.modelForm[item] = result[item];
                                };
                            };
                        }
                    });
                };
            },
            onSaveAuth() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        if (_.isNil(vm.modelForm.id)) {

                            AUTH_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            AUTH_EDIT_SERVICE(vm.modelForm).then(response => {
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
            }
        }
    };
</script>
<style lang="less" scoped>

</style>