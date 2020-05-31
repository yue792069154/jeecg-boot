<template>
    <div class="tool-menu-drop">
        <Dropdown :placement="placement" trigger="click" @on-click="onMenuClick">
            <div v-if="top" class="sc-dropdown-item" :id="menu.menuId">
                <template v-if="menu.menuIconProtCode=='img'">
                    <img :src="menu.menuIconProtContent" />
                </template>
                <template v-else>
                    <i :class="menu.menuIconProtContent" :style="menu.menuStyle"></i>
                </template>

                <template v-if="menu.statusCode==0">
                    <Tooltip content="已停用" placement="right">
                        <label v-if="menu.showMenuName" style="color: #8C8C8C;cursor: not-allowed;"
                            v-text="menu.menuName"></label>
                    </Tooltip>
                </template>
                <template v-else>
                    <label v-if="menu.showMenuName" :class="menu.menuClass" :style="menu.menuStyle"
                        v-text="menu.menuName"></label>
                </template>

                <template v-if="menu.children && menu.children.length > 0">
                    <Icon type="ios-arrow-down"></Icon>
                </template>
            </div>
            <div v-else class="sc-dropdown-item-child" :id="menu.menuId">
                <template v-if="menu.menuIconProtCode=='img'">
                    <img :src="menu.menuIconProtContent" />
                </template>
                <template v-else>
                    <i :class="menu.menuIconProtContent" :style="menu.menuStyle"></i>
                </template>


                <template v-if="menu.statusCode==0">
                    <Tooltip content="已停用" placement="right">
                        <label v-if="menu.showMenuName" style="color: #8C8C8C;cursor: not-allowed;"
                            v-text="menu.menuName"></label>
                    </Tooltip>
                </template>
                <template v-else>
                    <label v-if="menu.showMenuName" :class="menu.menuClass" :style="menu.menuStyle"
                        v-text="menu.menuName"></label>
                </template>



                <template v-if="menu.children && menu.children.length > 0">
                    <Icon type="ios-arrow-forward"></Icon>
                </template>
            </div>
            <Dropdown-Menu slot="list">
                <template v-for="child in menu.children">
                    <template v-if="child.children && child.children.length == 0">
                        <Dropdown-Item :disabled="child.statusCode==0" :name="child.menuId" :id="child.menuId">
                            <template v-if="child.menuIconProtCode=='img'">
                                <img :src="child.menuIconProtContent" />
                            </template>
                            <template v-else>
                                <i :class="child.menuIconProtContent" :style="child.menuStyle"></i>
                            </template>

                            <template v-if="menu.statusCode==0">
                                <Tooltip content="已停用" placement="right">
                                    <label v-if="child.showMenuName" style="color: #8C8C8C;cursor: not-allowed;"
                                        v-text="child.menuName"></label>
                                </Tooltip>
                            </template>
                            <template v-else>
                                <label v-if="child.showMenuName" :class="child.menuClass" :style="child.menuStyle"
                                    v-text="child.menuName"></label>
                            </template>

                        </Dropdown-Item>
                    </template>
                    <template v-else>
                        <tool-menu-drop :menu="child" :top="false"></tool-menu-drop>
                    </template>
                </template>
            </Dropdown-Menu>
        </Dropdown>
    </div>
</template>
<script>
    export default {
        name: "toolMenuDrop",
        props: {
            menu: {
                type: Object
            },
            top: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
                placement: "bottom-start"
            };
        },
        mounted() {
            if (!this.top) {
                this.placement = "right-start"
            };
        },
        methods: {
            onMenuClick(menuId) {
                this.$emit('on-select', menuId);
            }
        }
    };
</script>
<style lang="less" scoped>
    .tool-menu-drop {
        height: 100%;
        cursor: pointer;

        .ivu-dropdown {
            display: initial;
        }

        .ivu-dropdown .ivu-dropdown {
            width: auto;
        }

        .sc-dropdown-item {
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: center;
            padding: 0px 10px;
            width: auto;
            color: white;
            cursor: pointer;
            -webkit-transition: background 0.2s ease-in-out;
            transition: background 0.2s ease-in-out;

            i {
                margin-right: 6px;
                font-size: 14px;
            }

            &:hover {
                background-color: #363E4F;
            }

        }

        .sc-dropdown-item-child {
            margin: 0;
            line-height: normal;
            padding: 7px 16px;
            clear: both;
            color: #515a6e;
            font-size: 12px !important;
            white-space: nowrap;
            list-style: none;

            i {
                margin-top: -1px;
                font-size: 12px;
            }
        }

        .sc-dropdown-item img {
            width: 16px;
            height: 16px;
            vertical-align: middle;
        }

        .ivu-dropdown-item img {
            width: 16px;
            height: 16px;
            vertical-align: middle;
        }

    }
</style>