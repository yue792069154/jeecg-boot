<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="角色名称" prop="roleName">
            <Input placeholder="请输入角色名称" v-model="modelForm.roleName" clearable />
        </FormItem>
        <FormItem label="角色编码" prop="roleCode">
            <Input placeholder="请输入角色编码" v-model="modelForm.roleCode" clearable />
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        ROLE_QUERY_SERVICE,
        ROLE_ADD_SERVICE,
        ROLE_EDIT_SERVICE
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../store/mutations';

    export default {
        name:"roleSave",
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
                    roleName: null,
                    roleCode: null
                },
                modelFormRule: {
                    roleName: [{
                        required: true,
                        message: '请输入角色名称',
                        trigger: 'change,blur'
                    }],
                    roleCode: [{
                        required: true,
                        message: '请输入角色编码',
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
                    ROLE_QUERY_SERVICE({
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
            onSaveRole() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        if (_.isNil(vm.modelForm.id)) {

                            ROLE_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            ROLE_EDIT_SERVICE(vm.modelForm).then(response => {
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