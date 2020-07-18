<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="机构名称" prop="orgName">
            <Input placeholder="请输入机构名称" v-model="modelForm.orgName" clearable />
        </FormItem>
        <FormItem label="机构顺序" prop="sort">
            <InputNumber placeholder="请输入机构顺序" :max="9999" :min="0" v-model="modelForm.sort" style="width:100%">
            </InputNumber>
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        ORG_QUERY_SERVICE,
        ORG_ADD_SERVICE,
        ORG_EDIT_SERVICE
    } from "../../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';

    export default {
        name: "busOrgSave",
        components: {
           
        },
        props: {
            id: {
                type: String
            },
            orgParentId: {
                type: String
            }
        },
        data() {
            return {

                modelForm: {
                    id: null,
                    orgName: null,
                    sort: 1
                },
                modelFormRule: {
                    orgName: [{
                        required: true,
                        message: '请输入机构名称',
                        trigger: 'change,blur'
                    }]
                },

                IconModal: false

            }
        },
        mounted() {
            this.getOrg();
        },
        methods: {
            getOrg() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    ORG_QUERY_SERVICE({
                        id: id
                    }).then(response => {
                        if (response.success) {
                            var result = response.result;
                            if (!_.isNil(result)) {
                                for (var item in vm.modelForm) {
                                    vm.modelForm[item] = result[item];
                                };
                            };
                        }
                    });
                };
            },
            onSaveOrg() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        vm.modelForm.orgParentId = vm.orgParentId || vm.modelForm.orgParentId;

                        if (_.isNil(vm.modelForm.id)) {

                            ORG_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            ORG_EDIT_SERVICE(vm.modelForm).then(response => {
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
                this.modelForm.orgParentId = null;
            }
        }
    };
</script>
<style lang="less" scoped>

</style>