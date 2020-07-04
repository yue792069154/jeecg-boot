<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="字典类型分组" prop="dictTypeGroup">
            <Select placeholder="请选择字典类型分组" v-model="modelForm.dictTypeGroup">
                <Option v-for="item in dict.dictTypeGroup" :value="item.dictCode" :key="item.dictCode"
                    :label="item.dictName">
                </Option>
            </Select>
        </FormItem>
        <FormItem label="字典类型名称" prop="dictTypeName">
            <Input placeholder="请输入字典类型名称" v-model="modelForm.dictTypeName" clearable />
        </FormItem>
        <FormItem label="字典类型编码" prop="dictTypeCode">
            <Input placeholder="请输入字典类型编码" v-model="modelForm.dictTypeCode" clearable />
        </FormItem>
        <FormItem label="扩展表名称" prop="extendTableName">
            <Input placeholder="请输入扩展表名称" v-model="modelForm.extendTableName" clearable />
        </FormItem>
        <FormItem label="扩展表中文名" prop="extendTableChineseName">
            <Input placeholder="请输入扩展表中文名" v-model="modelForm.extendTableChineseName" clearable />
        </FormItem>
        <FormItem label="顺序" prop="sort">
            <InputNumber :max="10000" :min="0" v-model="modelForm.sort" style="width:100%"></InputNumber>
        </FormItem>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        DICT_TYPT_QUERY_SERVICE,
        DICT_TYPT_ADD_SERVICE,
        DICT_TYPT_EDIT_SERVICE,
        DICT_LIST_BY_DICT_TYPE_CODE_SERVICE
    } from "../../../axios/api";

    export default {
        name: "dictTypeSave",
        props: {
            id: {
                type: String
            },
        },
        data() {
            return {

                modelForm: {
                    id: null,
                    dictTypeGroup: null,
                    dictTypeName: null,
                    dictTypeCode: null,
                    extendTableName: null,
                    extendTableChineseName: null,
                    sort: 1
                },
                modelFormRule: {
                    dictTypeGroup: [{
                        required: true,
                        message: '请选择字典类型分组',
                        trigger: 'change,blur'
                    }],
                    dictTypeName: [{
                        required: true,
                        message: '请输入字典类型名称',
                        trigger: 'change,blur'
                    }],
                    dictTypeCode: [{
                        required: true,
                        message: '请输入字典类型编码',
                        trigger: 'blur'
                    }]
                },
                dict: {
                    dictTypeGroup: []
                }
            }
        },
        mounted() {
            this.getDict();
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
            getDict() {

                var vm = this;

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "dictTypeGroup"
                }).then(response => {
                    if (response.success) {
                        vm.dict.dictTypeGroup = response.result;
                    };
                });


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