<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="字典名称" prop="dicttypename">
            <Input placeholder="请输入字典名称" v-model="modelForm.dicttypename" clearable />
        </FormItem>
        <FormItem label="字典编码" prop="dictTypeCode">
            <Input placeholder="请输入字典编码" v-model="modelForm.dictTypeCode" clearable />
        </FormItem>
        <FormItem label="扩展表名称" prop="extendTableName">
            <Input placeholder="请输入扩展表名称" v-model="modelForm.extendTableName" clearable />
        </FormItem>
        <FormItem label="扩展表中文名" prop="extendTableChineseName">
            <Input placeholder="请输入扩展表中文名" v-model="modelForm.extendTableChineseName" clearable />
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        DICT_TYPT_EDIT_SERVICE,
        DICT_TYPT_ADD_SERVICE,
        DICT_TYPT_QUERY_SERVICE
    } from "../../axios/api";

    export default {
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
                    dictTypeName: null,
                    dictTypeCode: null,
                    extendTableName: null,
                    extendTableChineseName: null
                },
                modelFormRule: {
                    dictTypeName: [{
                        required: true,
                        message: '请输入字典名称',
                        trigger: 'change,blur'
                    }],
                    dictTypeCode: [{
                        required: true,
                        message: '请输入字典编码',
                        trigger: 'blur'
                    }]
                },

                dict: {
                    positionRank: []
                }

            }
        },
        watch: {
            id(newValue, oldValue) {
                this.getDictType();
            }
        },
        mounted() {

            this.getDictType();

        },
        methods: {
            getDictType() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    DICT_TYPT_QUERY_SERVICE({
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
            onSaveDictType() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        if (_.isNil(vm.modelForm.id)) {

                            DICT_TYPT_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            DICT_TYPT_EDIT_SERVICE(vm.modelForm).then(response => {
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