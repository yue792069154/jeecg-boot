<template>
    <Form label-position="top" ref="modelForm" :model="modelForm" :rules="modelFormRule">
        <FormItem label="菜单类型" prop="menuType">
            <Select placeholder="请选择菜单类型" v-model="modelForm.menuType">
                <Option v-for="item in dict.menuType" :value="item.dictCode" :key="item.dictCode"
                    :label="item.dictName">
                </Option>
            </Select>
        </FormItem>
        <FormItem label="菜单名称" prop="menuName">
            <Input placeholder="请输入菜单名称" v-model="modelForm.menuName" clearable />
        </FormItem>
        <FormItem label="菜单编码" prop="menuCode">
            <Input placeholder="请输入菜单编码" v-model="modelForm.menuCode" clearable />
        </FormItem>
        <FormItem label="菜单图标" prop="menuIconProtContent">
            <Input icon="ios-more" placeholder="请选择菜单图标" readonly v-model="modelForm.menuIconProtContent"
                @on-click="onIconSelect" />
        </FormItem>
        <FormItem label="菜单协议" prop="menuEntryProtCode" v-if="modelForm.menuType==0">
            <Select placeholder="请选择菜单协议" v-model="modelForm.menuEntryProtCode">
                <Option v-for="item in dict.menuEntryProtCode" :value="item.dictCode" :key="item.dictCode"
                    :label="item.dictName">
                </Option>
            </Select>
        </FormItem>

        <FormItem label="路由地址" prop="menuRouteContent" v-if="modelForm.menuType==0">
            <Input placeholder="请输入路由地址" v-model="modelForm.menuRouteContent" clearable />
        </FormItem>

        <FormItem label="菜单协议内容" prop="menuEntryProtContent" v-if="modelForm.menuType==0">
            <Input placeholder="请输入菜单协议内容" type="textarea" :rows="6" v-model="modelForm.menuEntryProtContent"
                clearable />
        </FormItem>
        <FormItem label="菜单打开方式" prop="menuOpenTypeCode" v-if="modelForm.menuType==0">
            <Select placeholder="请选择菜单打开方式" v-model="modelForm.menuOpenTypeCode">
                <Option v-for="item in dict.menuOpenTypeCode" :value="item.dictCode" :key="item.dictCode"
                    :label="item.dictName">
                </Option>
            </Select>
        </FormItem>
        <FormItem label="菜单顺序" prop="sort">
            <InputNumber placeholder="请输入菜单顺序" :max="9999" :min="0" v-model="modelForm.sort" style="width:100%">
            </InputNumber>
        </FormItem>
        <modal v-model="IconModal" width="730" @on-cancel="onIconModalCancel">
            <font-Icon-Select @onSelect="onSelect"></font-Icon-Select>
        </modal>
    </Form>
</template>
<script>
    import Vue from 'vue';
    import _ from 'lodash';
    import {
        MENU_QUERY_SERVICE,
        MENU_ADD_SERVICE,
        MENU_EDIT_SERVICE,
        DICT_LIST_BY_DICT_TYPE_CODE_SERVICE,
    } from "../../axios/api";
    import {
        Poptip
    } from 'view-design';
    import {
        ACCESS_TOKEN
    } from '../../store/mutations';

    import fontIconSelect from '../../components/font-icon-select';
    export default {
        name: "menuSave",
        components: {
            fontIconSelect
        },
        props: {
            id: {
                type: String
            },
            parentMenuId: {
                type: String
            }
        },
        data() {
            return {

                modelForm: {
                    id: null,
                    parentMenuId: null,
                    menuName: null,
                    menuCode: null,
                    menuType: null,
                    menuIconProtContent: null,
                    menuEntryProtCode: null,
                    menuEntryProtContent: null,
                    menuOpenTypeCode: null,
                    menuRouteContent: null,
                    sort: 1
                },
                modelFormRule: {
                    menuName: [{
                        required: true,
                        message: '请输入菜单名称',
                        trigger: 'change,blur'
                    }],
                    menuCode: [{
                        required: true,
                        message: '请输入菜单编码',
                        trigger: 'change,blur'
                    }],
                    menuType: [{
                        required: true,
                        message: '请选择菜单类型',
                        trigger: 'change,blur'
                    }]
                },

                dict: {
                    menuOpenTypeCode: [],
                    menuEntryProtCode: [],
                    menuType: []
                },

                IconModal: false

            }
        },
        mounted() {
            this.getDict();
            this.getMenu();
        },
        methods: {
            getMenu() {

                var vm = this;

                var id = this.id;

                if (!_.isNil(id)) {
                    MENU_QUERY_SERVICE({
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
            getDict() {

                var vm = this;

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "menuOpenType"
                }).then(response => {
                    if (response.success) {
                        vm.dict.menuOpenTypeCode = response.result;
                    };
                });

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "menuEntryProt"
                }).then(response => {
                    if (response.success) {
                        vm.dict.menuEntryProtCode = response.result;
                    };
                });

                DICT_LIST_BY_DICT_TYPE_CODE_SERVICE({
                    dictTypeCode: "menuType"
                }).then(response => {
                    if (response.success) {
                        vm.dict.menuType = response.result;
                    };
                });

            },
            onSaveMenu() {

                var vm = this;

                this.$refs['modelForm'].validate((valid) => {
                    if (valid) {

                        vm.modelForm.parentMenuId = vm.parentMenuId;
                        var menuType = vm.modelForm.menuType;
                        if (menuType == 1) {

                            vm.modelForm.menuEntryProtCode = "";
                            vm.modelForm.menuEntryProtContent = "";
                            vm.modelForm.menuOpenTypeCode = "";
                            vm.modelForm.menuRouteContent = "";

                        };

                        if (_.isNil(vm.modelForm.id)) {



                            MENU_ADD_SERVICE(vm.modelForm).then(response => {
                                if (response.success) {

                                    vm.$Message.success('添加成功');
                                    vm.$emit("on-save-success");
                                    vm.resetFields();

                                }
                            });

                        } else {

                            MENU_EDIT_SERVICE(vm.modelForm).then(response => {
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
            onIconSelect(e) {
                this.IconModal = true;
            },
            onIconModalCancel() {
                this.IconModal = false;
            },
            onSelect(value) {
                this.modelForm.menuIconProtContent = value;
                this.IconModal = false;
            },
            resetFields() {
                this.$refs.modelForm.resetFields();
                this.modelForm.id = null;
                this.modelForm.parentMenuId = null;
            }
        }
    };
</script>
<style lang="less" scoped>

</style>