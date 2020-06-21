<template>
    <div class="layout">
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
                <div class="layout-nav-header-menu">
                    <i-Menu mode="horizontal" :active-name="menuHeaderActiveName" ref="refMenu"
                        @on-select="onHeaderMenuSelect">
                        <Menu-Item :name="item.id" :key="index" v-for="(item,index) in menuList">
                            <Icon :type="item.menuIconProtContent"></Icon>
                            {{item.menuName}}
                        </Menu-Item>
                    </i-Menu>
                </div>
            </div>

        </i-header>
        <layout>
            <div class="layout-nav-child-pannel">
                <div class="layout-nav-child" v-show="menuHeaderActive.menuShowSider">
                    <sider-menu mode="horizontal" :accordion="false" theme="light" ref="refSiderMenu"
                        :menu-list="menuSiderList" :menu-active-name="menuSiderActiveName"
                        :menu-open-names="menuSiderOpenNames" :menu-collapsed=collapsed @on-select="onSiderMenuSelect">
                    </sider-menu>
                </div>
            </div>
            <i-Content class="layout-content">
                <div class="layout-content-window">
                    <page-View ref="pageView"></page-View>
                </div>

            </i-Content>
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
    import {
        MENU_USER_LIST_SERVICE
    } from "../../../axios/api";
    import router from '../../../router';
    import pageView from '../../../components/page-view';
    import TreeImpl from '../../../libs/fw-core/fw-tree/tree';
    export default {
        components: {
            siderMenu,
            tagMenu,
            pageView
        },
        data() {
            return {
                collapsed: false,

                topTitle: '快速开发平台',
                topSubTitle: '管理员',
                navTitle: '系统管理',


                menuTreeImpl: null,


                menuList: [],
                menuSiderList: [],
                menuSelectList: [],

                menuActive: {},
                menuHeaderActive: {
                    menuShowSider: true
                },

                menuSiderOpenNames: [],
                menuSiderActiveName: '',
                menuHeaderActiveName: '',

                menuView: null,
                menuViewFrame: null,

                urlParams: {},

                openInterface: null

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

                    var menuList = response.result;

                    if (!_.isNil(menuList)) {

                        this.menuTreeImpl = new TreeImpl(menuList, 'id', 'parentMenuId', 'children');
                        this.menuList = this.menuTreeImpl.treeList;

                        var firstLeafMenu = this.menuTreeImpl.getFirstLeafNode();
                        if (!_.isNil(firstLeafMenu)) {
                            this.onHeaderMenuSelect(firstLeafMenu.id);
                        }
                    }

                });


            },
            _openPage() {

                var vm = this;
                this.$refs.pageView.openPage(this.menuActive);


                //this.$router.push('/system/' + (this.menuActive.menuRouteContent || this.menuActive.id));

            },
            onHeaderMenuSelect(menuId) {

                this.menuSiderList = [];
                this.menuSiderActiveName = '';

                var menu = this.menuTreeImpl.getTreeByIdFieldValue(menuId);
                if (!_.isNil(menu)) {
                    if (!_.isNil(menu.children) && menu.children.length > 0) {
                        this.menuSiderList = menu.children || [];
                        menu.menuShowSider = true;
                        var firstLeafMenu = new TreeImpl(menu, 'id', 'parentMenuId', 'children')
                            .getFirstLeafNode();
                        this.onSiderMenuSelect(firstLeafMenu.id);
                    } else {
                        menu.menuShowSider = false;
                        this.onSiderMenuSelect(menu.id);
                    };
                    this.menuHeaderActive = menu;
                };

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
                        vm.menuSiderOpenNames.push(item.menuId);
                    });
                } else {
                    this.menuHeaderActiveName = menuId;
                }


                this.menuSiderActiveName = menuId;


                this.$nextTick(function () {
                    this.$refs.refMenu.updateActiveName();
                });


            },
            onToolMenuSelect(menuId) {

                var menu = this.pageToolMenuTreeImpl.getTreeByIdFieldValue(menuId);

                this.menuActive = {};

                var menuSelect = _.find(this.menuSelectList, function (item) {
                    return item.menuId == menuId;
                });

                if (!_.isNil(menuSelect)) {
                    this.menuSelectList.push(menu);

                } else {
                    menu = menuSelect;
                }

                _.forEach(this.menuSelectList, function (item) {
                    item.menuActive = false;
                    item.menuActiveColor = 'default';
                });


                menu.menuActive = true;
                menu.menuActiveColor = 'primary';

                this.menuActive = menu;
                this._openPage();

                this.$refs.refTagMenu.onUpdateMenuView();

            }
        }
    };
</script>
<style lang="less">
    @import "./main.less";
</style>