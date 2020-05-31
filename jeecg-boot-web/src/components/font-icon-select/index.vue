<template>
    <div class="com-font">
        <div class="com-font-search">
            <input type="text" placeholder="查询图标..." autofocus="" v-on:input="onInputEnter">
        </div>
        <ul>
            <li :class="{ active: icon.active }" v-for="icon in iconFilterList" @click="onFontClick(icon)">
                <span :class="icon.value"></span>
            </li>
        </ul>
    </div>
</template>

<script>
    import _ from "lodash";
    import iconfont from './iconfont/iconfont.json';
    import './iconfont/iconfont.css';

    export default {
        name: "fontIconSelect",
        data() {
            return {
                iconList: [],
                iconFilterList: []
            };
        },
        mounted() {

            var vm = this;

            _.forEach(iconfont.glyphs, function (icon) {
                vm.iconList.push({
                    label: icon.name,
                    value: "icon iconfont icon" + icon.font_class,
                    active: false
                });
            });

            this.iconFilterList = this.iconList;

        },
        methods: {
            onInputEnter(e) {

                this.iconFilterList = _.filter(this.iconList, function (icon) {
                    if (icon.label.indexOf(e.target.value) != -1 || icon.value.indexOf(e.target.value) != -1) {
                        return icon;
                    }
                });

            },
            onFontClick(icon) {
                _.forEach(this.iconList, function (icon) {
                    icon.active = false;
                });
                icon.active = true;

                this.$emit('onSelect', icon.value);

            }
        }
    }
</script>

<style lang="less" scoped>
    .com-font {
        .com-font-search {
            margin-bottom: 10px;
            display: flex;
            position: relative;
            align-items: center;
            justify-content: center;

            input {
                width: 60%;
                font-weight: 400;
                font-family: Eina;
                font-size: 16px;
                border: 0;
                outline: 0;
                border-radius: 6px;
                -webkit-box-sizing: border-box;
                box-sizing: border-box;
                display: block;
                -webkit-appearance: none;
                -webkit-transition: -webkit-box-shadow .3s;
                transition: -webkit-box-shadow .3s;
                transition: box-shadow .3s;
                transition: box-shadow .3s, -webkit-box-shadow .3s;
                font-size: 16px;
                line-height: 22px;
                padding: 20px;
                padding-left: 50px;
                padding-right: 50px;
                background-color: #fff;
                -webkit-box-shadow: 0 0px 6px 0 rgba(0, 0, 0, .1), 0 1px 3px 0 rgba(0, 0, 0, .08);
                box-shadow: 0 0px 6px 0 rgba(0, 0, 0, .1), 0 1px 3px 0 rgba(0, 0, 0, .08);
            }
        }

        ul {
            li {
                display: inline-block;
                vertical-align: middle;
                width: 64px;
                height: 64px;
                padding: 12px 0;
                margin-right: -1px;
                margin-bottom: -1px;
                border: 1px solid #e2e2e2;
                text-align: center;
                color: #666;
                transition: all 0.3s;
                -webkit-transition: all 0.3s;
                cursor: pointer;

                &:hover {
                    background-color: #f2f2f2;
                    color: #000;
                }

                &.active {
                    background-color: #f2f2f2;
                    color: #409EFF;
                }

                span {
                    font-size: 24px;
                }

                div {
                    width: 64px;
                    height: 20px;
                    font-size: 12px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }
            }
        }
    }
</style>