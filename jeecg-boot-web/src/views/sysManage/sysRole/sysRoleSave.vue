<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="角色名称" prop="roleName">
            <Input placeholder="请输入角色名称" v-model="modelForm.roleName" clearable />
        </FormItem>
        <FormItem label="角色编码" prop="roleCode">
            <Input placeholder="请输入角色编码" v-model="modelForm.roleCode" clearable />
        </FormItem>
         <FormItem label="角色入口协议" prop="roleEntryProtCode">
            <Select placeholder="请选择角色入口协议" v-model="modelForm.roleEntryProtCode">
                <Option v-for="item in dict.roleEntryProtCode" :value="item.dictCode" :key="item.dictCode"
                    :label="item.dictName">
                </Option>
            </Select>
        </FormItem>
         <FormItem label="角色入口" prop="roleEntryProtContent">
            <Input placeholder="请输入角色入口" type="textarea" :rows="6" v-model="modelForm.roleEntryProtContent" clearable />
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        ROLE_QUERY_SERVICE,
        ROLE_ADD_SERVICE,
        ROLE_EDIT_SERVICE,
        DICT_LIST_BY_DICT_TYPE_CODE_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';

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
                    roleCode: null,
                    roleEntryProtCode:null,
                    roleEntryProtContent:null
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
                },
                dict: {
                    roleEntryProtCode: []
                }

            }
        },
        mounted() {
            this.getDict();
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
            getDict() {

                var vm = this;

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "menuEntryProt"
                }).then(response => {
                    if (response.success) {
                        vm.dict.roleEntryProtCode = response.result;
                    };
                });

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