<template>
    <div class="sider-menu">
        <Menu v-show="!menuCollapsed" :theme="theme" width="220" :mode="mode" :active-name="menuActiveName"
            :open-names="menuOpenNames" :accordion="accordion" ref="siderMenu" @on-select="onMenuSelect">
            <sider-menu-item :menu-list="menuList" @on-select="onMenuSelect"></sider-menu-item>
        </Menu>
        <div v-show="menuCollapsed">
            <div class="sider-menu-panel">
                <sider-menu-collapsed :menu-list="menuList" @on-select="onMenuSelect"></sider-menu-collapsed>
            </div>
        </div>
    </div>
</template>
<script>
    import siderMenuItem from '../sider-menu-item';
    import siderMenuCollapsed from '../sider-menu-collapsed';
    export default {
        name: 'siderMenu',
        components: {
            siderMenuItem,
            siderMenuCollapsed
        },
        props: {
            menuList: {
                type: Array,
                default () {
                    return []
                }
            },
            menuCollapsed: {
                type: Boolean
            },
            menuActiveName: {
                type: String
            },
            menuOpenNames: {
                type: Array,
                default () {
                    return []
                }
            },
            mode: {
                type: String,
                default () {
                    return "vertical"
                }
            },
            theme: {
                type: String,
                default () {
                    return "dark"
                }
            },
            accordion: {
                type: Boolean,
                default () {
                    return true
                }
            }
        },
        data() {
            return {

            };
        },
        mounted() {

        },
        methods: {
            onMenuSelect(menuId) {
                this.$emit('on-select', menuId);
            },
            onMenuOpenNamesUpdate() {
                this.$nextTick(function () {
                    this.$refs.siderMenu.updateOpened();
                });
            },
            onMenuActiveNameUpdate() {
                this.$nextTick(function () {
                    this.$refs.siderMenu.updateActiveName();
                });
            }
        }
    };
</script>
<style lang="less" scoped>
    .sider-menu {

        position: relative;
        z-index: 1;

        .sider-menu-panel {

            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;

            .sider-menu-drop-a {
                display: inline-block;
                padding: 8px 22px;
                width: 100%;
                text-align: center;
                color: #495060;

                i {
                    font-size: 20px;
                    color: white;
                }
            }

        }
    }
</style>