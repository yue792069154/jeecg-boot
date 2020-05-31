<template>
    <div class="layout">
        <layout>
            <i-header>
                <div class="layout-tools">
                    <label v-text="topTitle"></label>
                    <label class="layout-tools-sub-title" v-if="topSubTitle != null || undefined"
                        v-text="topSubTitle"></label>
                </div>
                <div class="layout-nav">
                    <div class="layout-nav-title">
                        <div v-text="navTitle"></div>
                    </div>
                    <a class="layout-nav-sider-trigger" @click="collapsed = !collapsed">
                        <Icon type="md-menu" :class="{'sc-sider-trigger':collapsed}" />
                    </a>
                </div>
            </i-header>
            <layout>
                <Sider collapsible hide-trigger :collapsed-width="64" width="211" v-model="collapsed">
                    <sider-menu ref="refSiderMenu" :menu-list="menuSiderList" :menu-active-name="menuSiderActiveName"
                        :menu-open-names="menuSiderOpenNames" :menu-collapsed=collapsed @on-select="onSiderMenuSelect">
                    </sider-menu>
                </Sider>
                <i-Content class="layout-content">
                    <tag-menu ref="refTagMenu" :menu-list="menuSelectList" @on-select="onSiderMenuSelect"
                        @on-close="onSiderMenuClose"></tag-menu>
                    <div class="layout-content-window">
                        <router-view></router-view>
                    </div>
                </i-Content>
            </layout>
        </layout>
    </div>
</template>
<script>
    import Vue from 'vue';
    import {
        ACCESS_TOKEN
    } from '../../../store/mutations';
    import _ from 'lodash';
    import siderMenu from "../../../components/sider-menu";
    import tagMenu from "../../../components/tag-menu";
    import TreeImpl from "../../../libs/tree";
    import Qs from "../../../libs/qs";
    import {
        MENU_USER_LIST_SERVICE
    } from "../../../axios/api";

    export default {
        components: {
            siderMenu,
            tagMenu
        },
        data() {
            return {
                collapsed: false,

                topTitle: '快速开发平台',
                topSubTitle: '管理员',
                navTitle: '工作台',

                menuTreeImpl: null,

                menuList: [],
                menuSiderList: [],
                menuSelectList: [],

                menuActive: {},

                menuSiderOpenNames: [],
                menuSiderActiveName: '',

                urlParams: {},

            };
        },
        mounted() {
            this.init();
        },
        methods: {
            init() {

                MENU_USER_LIST_SERVICE({
                    token: Vue.ls.get(ACCESS_TOKEN)
                }).then(response => {

                    var menuList = response.result.menuList;

                    if (!_.isNil(menuList)) {

                        this.menuTreeImpl = new TreeImpl(menuList, 'id', 'parentId', 'children');
                        this.menuSiderList = this.menuTreeImpl.treeList;
                    }

                });


            },
            _openPage() {

                var vm = this;

                this.ss = this.menuActive;

            },
            onSiderMenuSelect(menuId) {

                var vm = this;

                this.menuActive = {};

                _.forEach(this.menuSelectList, function (item) {
                    item.menuActive = false;
                    item.menuActiveColor = 'default';
                });

                var menu = _.find(this.menuSelectList, function (item) {
                    return item.id == menuId;
                });

                if (_.isNil(menu)) {

                    menu = this.menuTreeImpl.getTreeByIdFieldValue(menuId);
                    this.menuSelectList.push(menu);
                }

                this.$set(menu, "menuActive", true);
                this.$set(menu, "menuActiveColor", 'primary');


                this.menuActive = menu;
                this._openPage();

                this.menuSiderOpenNames = [];

                var parentMenuList = this.menuTreeImpl.getAncestors(menuId) || [];
                if (!_.isNil(parentMenuList)) {
                    _.forEach(parentMenuList, function (item, index) {
                        vm.menuSiderOpenNames.push(item.id);
                    });
                }

                this.menuSiderActiveName = menuId;

                this.$refs.refSiderMenu.onMenuOpenNamesUpdate();
                this.$refs.refSiderMenu.onMenuActiveNameUpdate();

                this.$refs.refTagMenu.onUpdateMenuView();

            },
            onSiderMenuClose: function (delMenuList, menuList, action) {

                if (!_.isNil(menuList)) {

                    this.menuSiderOpenNames = [];
                    this.menuSiderActiveName = '';

                    this.$refs.refSiderMenu.onMenuOpenNamesUpdate();
                    this.$refs.refSiderMenu.onMenuActiveNameUpdate();
                };

                this.menuSelectList = menuList;
            }
        }
    };
</script>
<style lang="less" scoped>
    @import "./main.less";
</style>