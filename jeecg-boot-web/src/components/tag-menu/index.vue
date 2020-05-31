<template>
    <div class="tag-menu">
        <div class="tag-menu-panel">
            <div class="tag-menu-item">
                <div class="tag-menu-item-close">
                    <Dropdown @on-click="onMenuActionClick" @on-visible-change="onMenuActionVisibleChange">
                        <i-Button>
                            <Icon type="ios-close-circle-outline"></Icon>
                        </i-Button>
                        <Dropdown-Menu slot="list">
                            <Dropdown-Item :name="item.actionTitleId" :disabled="item.disabled"
                                v-for="item in menuActionList">{{ item.actionTitle }}</Dropdown-Item>
                        </Dropdown-Menu>
                    </Dropdown>
                </div>
                <ul v-show="visible" :style="{left: contextMenuLeft + 'px', top: contextMenuTop + 'px'}"
                    class="sc-layout-nav-tag-contextmenu">
                    <li v-for="item in menuActionList" @click="onMenuActionClick(item.actionTitleId)">
                        {{item.actionTitle}}</li>
                </ul>
                <div class="ivu-btn-left">
                    <i-Button @click="onDirectionBtnClick(240)">
                        <Icon type="ios-arrow-back"></Icon>
                    </i-Button>
                </div>
                <div class="ivu-btn-right">
                    <i-Button @click="onDirectionBtnClick(-240)">
                        <Icon type="ios-arrow-forward"></Icon>
                    </i-Button>
                </div>
                <div class="tag-menu-scroll-outer" ref="scrollOuter">
                    <div class="tag-menu-scroll-body" ref="scrollBody" :style="{ left: tagBodyLeft + 'px' }">
                        <Tag type="dot" ref="refMenu" :data-route-item="item" :key="index" closable
                            :color="item.menuActiveColor" v-for="(item,index) in menuList"
                            @click.native="onMenuClick(item)" @on-close="onMenuClose(item)"
                            @contextmenu.prevent.native="onContextMenu($event)">{{item.name}}</Tag>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import _ from 'lodash';
     import TreeImpl from "../../libs/tree";

    export default {
        name: "tagMenu",
        props: {
            menuList: {
                type: Array,
                default () {
                    return []
                }
            }
        },
        data() {
            return {
                visible: false,

                menuSelect: {},
                menuTreeImpl: null,

                menuActionList: [{
                    actionTitle: "关闭当前菜单",
                    actionTitleId: "close-this",
                    disabled: false
                }, {
                    actionTitle: "关闭其他菜单",
                    actionTitleId: "close-other",
                    disabled: false
                }, {
                    actionTitle: "关闭右侧菜单",
                    actionTitleId: "close-right",
                    disabled: false
                }, {
                    actionTitle: "关闭所有菜单",
                    actionTitleId: "close-all",
                    disabled: false
                }],

                tagBodyLeft: 0,
                outerPadding: 4,

                contextMenuLeft: 0,
                contextMenuTop: 0
            };
        },
        watch: {
            menuList: {
                immediate: true,
                handler(val) {
                    this.menuTreeImpl = new TreeImpl(val, 'id', 'parentId', 'children')
                }
            }
        },
        mounted() {

        },
        methods: {
            onMenuClick(menu) {

                _.forEach(this.menuList, function (item) {
                     item.menuActive = false;
                    item.menuActiveColor = 'default';
                });

                this.$set(menu, "menuActive", true);
                this.$set(menu, "menuActiveColor", 'primary');

                this.$emit('on-select', menu.id);

            },
            onMenuClose(menu) {

                var offset = 1;
                var index = _.findIndex(this.menuList, function (item) {
                    return item.id == menu.id;
                });

                if (menu.menuActive === true) {
                    if (this.menuList.length != 1) {
                        if (this.menuList.length > 0 && index != 0) {
                            offset = 1;
                        } else {
                            offset = -1;
                        };
                        this.onMenuClick(this.menuList[index - offset]);
                    };
                };

                this.menuList.splice(index, 1);

                this.$emit('on-close', [menu], this.menuList, 'close-this');

            },
            onMenuActionVisibleChange() {
                var index = _.findIndex(this.menuList, function (item) {
                    return item.menuActive == true;
                });
                if (index == this.menuList.length - 1) {
                    this.menuActionList[2].disabled = true;
                } else {
                    this.menuActionList[2].disabled = false;
                };
            },
            onMenuActionClick(action) {

                var me = this;
                let menuCloneList = _.merge({}, this.menuList);
                let menuDelList = [];

                switch (action) {
                    case 'close-this':
                        this.onMenuClose(_.find(menuCloneList, function (item) {
                            return item.menuActive == true;
                        }));
                        break;
                    case 'close-all':
                        this.menuList = [];
                        menuDelList = menuCloneList;
                        break;
                    case 'close-right':
                        var index = _.findIndex(this.menuList, function (item) {
                            return item.menuActive == true;
                        });
                        this.menuList.splice(index + 1, this.menuList.length - (index + 1));
                        break;
                    case 'close-other':
                        this.menuList = [];
                        _.forEach(menuCloneList, function (item) {
                            if (item.menuActive === true) {
                                me.menuList = [item];
                            } else {
                                menuDelList.push(item)
                            };
                        });
                        break;
                    default:
                        break;
                };

                this.tagBodyLeft = 0;

                this.$emit('on-close', menuDelList, this.menuList, action);

            },
            onUpdateMenuView() {

                var me = this;
                setTimeout(() => {
                    this.refMenuList = this.$refs.refMenu;
                    _.forEach(this.refMenuList, function (item, index) {
                        if (item.$attrs['data-route-item'].menuActiveColor == 'primary') {
                            let tag = me.refMenuList[index].$el
                            const outerWidth = me.$refs.scrollOuter.offsetWidth
                            const bodyWidth = me.$refs.scrollBody.offsetWidth
                            if (bodyWidth < outerWidth) {
                                me.tagBodyLeft = 0
                            } else if (tag.offsetLeft < -me.tagBodyLeft) {
                                // 标签在可视区域左侧
                                me.tagBodyLeft = -tag.offsetLeft + me.outerPadding
                            } else if (tag.offsetLeft > -me.tagBodyLeft && tag.offsetLeft + tag
                                .offsetWidth < -me.tagBodyLeft + outerWidth) {
                                // 标签在可视区域
                                me.tagBodyLeft = Math.min(0, outerWidth - tag.offsetWidth - tag
                                    .offsetLeft - me.outerPadding)
                            } else {
                                // 标签在可视区域右侧
                                me.tagBodyLeft = -(tag.offsetLeft - (outerWidth - me.outerPadding - tag
                                    .offsetWidth))
                            };
                        };
                    });
                }, 200);

            },
            onDirectionBtnClick(offset) {
                const outerWidth = this.$refs.scrollOuter.offsetWidth;
                const bodyWidth = this.$refs.scrollBody.offsetWidth;
                if (offset > 0) {
                    this.tagBodyLeft = Math.min(0, this.tagBodyLeft + offset)
                } else {
                    if (outerWidth < bodyWidth) {
                        if (this.tagBodyLeft < -(bodyWidth - outerWidth)) {
                            this.tagBodyLeft = this.tagBodyLeft;
                        } else {
                            this.tagBodyLeft = Math.max(this.tagBodyLeft + offset, outerWidth - bodyWidth)
                        }
                    } else {
                        this.tagBodyLeft = 0
                    }
                };
            }
        }
    };
</script>
<style lang="less" scoped>
    .tag-menu {

        position: relative;
        z-index: 0;

        .tag-menu-panel {
            padding: 0;
            height: 42px;
            background: #f0f0f0;

            .tag-menu-item {
                position: relative;
                border-bottom: 1px solid #f0f0f0;
                -webkit-touch-callout: none;
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;

                .ivu-btn {
                    border-radius: 0px;
                    width: 32px;
                    height: 40px;
                    text-align: center;
                    font-size: 18px;
                    padding: 0px;
                    border: 0px;
                    margin-top: 1px;
                }

                .tag-menu-item-close {
                    position: absolute;
                    top: 0;
                    height: 100%;
                    background: #fff;
                    z-index: 1;
                    right: 0;
                    width: 32px;
                    text-align: center;
                }

                .ivu-btn-left {
                    position: absolute;
                    top: 0;
                    height: 100%;
                    background: #fff;
                    z-index: 1;
                }

                .ivu-btn-right {
                    position: absolute;
                    top: 0;
                    right: 34px;
                    height: 100%;
                    background: #fff;
                    z-index: 1;
                }

                .tag-menu-scroll-outer {
                    position: absolute;
                    left: 32px;
                    right: 64px;
                    top: 0;
                    height: 42px;
                    bottom: 0;
                    -webkit-box-shadow: 0 0 3px 2px hsla(0, 0%, 39.2%, .1) inset;
                    box-shadow: inset 0 0 3px 2px hsla(0, 0%, 39.2%, .1);

                    .tag-menu-scroll-body {
                        height: calc(~"100% - 1px");
                        display: inline-block;
                        padding: 3px 4px 0;
                        position: absolute;
                        white-space: nowrap;
                        -webkit-transition: left .3s ease;
                        transition: left .3s ease;

                        .ivu-tag {
                            margin: 2px 3px 2px 0;
                        }
                    }
                }
            }
        }
    }
</style>