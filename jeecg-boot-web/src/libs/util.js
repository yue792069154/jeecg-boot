import _ from 'lodash';
import {
    downLoadFileAction
} from '../axios/action';
import {
    Message
} from 'view-design';

let util = {

};

function getRandomCode() {
    return Number(Math.random().toString().substr(3, 6) + Date.now()).toString(36);
}

util.title = function (title) {
    title = title ? title : '快速开发平台';
    window.document.title = title;
};
 

util.importXls = function () {

};



export default util;