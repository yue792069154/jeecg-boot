import _ from 'lodash';
import {
    downLoadFileAction
} from '../axios/action';
import {
    Message
} from 'view-design';
import appSettings from "../common/appSettings";

export const ExcelMixins = {
    data() {
        return {

        };
    },
    created() {

    },
    methods: {
        onExportXls(config) {

            var exportConfig = Object.assign({
                fileName: "",
                ids: [],
                url: ""
            }, config);

            let params = {};

            if (!_.isNil(exportConfig.fileName) && _.isString(exportConfig.fileName)) {
                if (!_.isNil(exportConfig.ids)) {
                    params['selections'] = exportConfig.ids.join(",");
                }
            } else {
                exportConfig.fileName = getRandomCode();
            }

            downLoadFileAction(exportConfig.url, null, params).then((response) => {
                if (!response) {
                    Message.warning("文件导出失败");
                    return false;
                }
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(new Blob([response]), fileName + '.xls');
                } else {
                    let url = window.URL.createObjectURL(new Blob([response]));
                    let link = document.createElement('a');
                    link.style.display = 'none';
                    link.href = url;
                    link.setAttribute('download', exportConfig.fileName + '.xls');
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                    window.URL.revokeObjectURL(url);
                }
            });
        },
        onImportXls(response, file, fileList, callback) {

            if (file.status === 'finished') {
                if (response.success) {
                    if (file.response.code === 201) {

                        let {
                            message,
                            result: {
                                msg,
                                fileUrl,
                                fileName
                            }
                        } = file.response;

                        let href = appSettings.jeecg.serviceSiteRootUrl + fileUrl;

                        this.$Message.info({
                            render: h => {
                                return h('div', [
                                    h('span', {}, [msg]),
                                    h('span', {}, [h("span", {}, ["具体详情请"]), h(a, {
                                        attrs: {
                                            href: href,
                                            target: "_blank",
                                            download: fileName
                                        },
                                    }, ["点击下载"])]),

                                ]);
                            }
                        });

                    } else {
                        this.$Message.success(file.response.message || `${file.name} 文件导入成功`);
                        callback();
                    }

                } else {

                    this.$Message.error({
                        content: `${file.name} ${file.response.message}`,
                        duration: 5
                    });

                }
            } else if (file.status === 'error') {
                this.$Message.error({
                    content: `文件导入失败: ${file.msg} `,
                    duration: 5
                });
            }
        }
    }

};