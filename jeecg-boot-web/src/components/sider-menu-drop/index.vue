<template>
    <div class="sider-menu-drop">
        <Dropdown transfer transfer-class-name="sider-menu-drop" placement='right-start' @on-click="onMenuClick">
            <a type="text" class="sc-dropdown-item" v-if="hideTitle">
                <template v-if="menu.menuIconProtCode=='img'">
                    <img :src="menu.menuIconProtContent" />
                </template>
                <template v-else>
                    <i :class="menu.menuIconProtContent"></i>
                </template>


                <template v-if="menu.statusCode==0">
                    <Tooltip content=" 已停用" placement="right">
                        <label style="color: #8C8C8C;cursor: not-allowed;" v-text="menu.name"></label>
                    </Tooltip>
                </template>
                <template v-else>
                    <label :class="menu.menuClass" :style="menu.menuStyle" v-text="menu.name"></label>
                </template>


                <template v-if="menu.children && menu.children.length > 0">
                    <Icon type="ios-arrow-forward"></Icon>
                </template>
            </a>
            <a type="text" v-else class="sc-drop-menu-a">
                <template v-if="menu.menuIconProtCode=='img'">
                    <img :src="menu.menuIconProtContent" />
                </template>
                <template v-else>
                    <i :class="menu.menuIconProtContent"></i>
                </template>
            </a>
            <Dropdown-Menu slot="list">
                <template v-for="child in menu.children">
                    <template v-if="child.children && child.children.length == 0">
                        <Dropdown-Item :disabled="child.statusCode==0" :name="child.id">
                            <template v-if="child.menuIconProtCode=='img'">
                                <img :src="child.menuIconProtContent" />
                            </template>
                            <template v-else>
                                <i :class="child.menuIconProtContent"></i>
                            </template>

                            <template v-if="child.statusCode==0">
                                <Tooltip content=" 已停用" placement="right">
                                    <label style="color: #8C8C8C;cursor: not-allowed;" v-text="child.menuName"></label>
                                </Tooltip>
                            </template>
                            <template v-else>
                                <label :class="child.menuClass" :style="child.menuStyle"
                                    v-text="child.menuName"></label>
                            </template>

                        </Dropdown-Item>
                    </template>
                    <template v-else>
                        <sider-menu-drop :menu="child" :hide-title="true"></sider-menu-drop>
                    </template>
                </template>
            </Dropdown-Menu>
        </Dropdown>
    </div>
</template>
<script>
    import siderMenuDrop from '../sider-menu-drop';
    export default {
        name: "siderMenuDrop",
        components: {
            siderMenuDrop
        },
        props: {
            menu: {
                type: Object
            },
            hideTitle: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {

            };
        },
        mounted() {

        },
        methods: {
            onMenuClick(id) {
                this.$emit('on-select', id);
            }
        }
    };
</script>
<style lang="less" scoped>
    .sider-menu-drop {

        padding: 7px 0px;

        .sc-dropdown-item {
            margin: 0;
            line-height: normal;
            padding: 7px 16px;
            clear: both;
            color: #515a6e;
            font-size: 12px !important;
            white-space: nowrap;
            list-style: none;
            cursor: pointer;
            -webkit-transition: background 0.2s ease-in-out;
            transition: background 0.2s ease-in-out;
        }

        .sc-drop-menu-a {
            display: inline-block;
            padding: 8px 22px;
            width: 100%;
            text-align: center;
            color: #495060;

            i {
                font-size: 20px;
                color: white;
            }

            label {
                cursor: pointer;
                font-size: 20px;
                color: white;
            }


        }


        img {
            max-width: 16px;
            min-width: 16px;
        }


    }
</style>