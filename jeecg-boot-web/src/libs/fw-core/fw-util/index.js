const RandomUtils = {
    getUuid: function () { //do
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
};

const ObjectUtils = {
    hasValue: function (obj) {
        if (_.isNil(obj)) {
            return false;
        }

        if (_.isBoolean(obj)) {
            return true;
        }

        if (_.isNumber(obj)) {
            return true;
        }

        if (_.isDate(obj)) {
            return true;
        }

        if (_.isString(obj)) {
            obj = _.trim(obj);
        }

        var has = !_.isEmpty(obj);
        return has;
    }
};

const DateUtils = {
    addYears: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date((_DateTime.getFullYear() + value), _DateTime.getMonth(), _DateTime.getDate(), _DateTime.getHours(), _DateTime.getMinutes(), _DateTime.getSeconds());
        }
        return _DateTime;
    },
    addMonths: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date(_DateTime.getFullYear(), (_DateTime.getMonth()) + value, _DateTime.getDate(), _DateTime.getHours(), _DateTime.getMinutes(), _DateTime.getSeconds());
        }
        return _DateTime;
    },
    addWeeks: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date(Date.parse(_DateTime) + (86400000 * 7 * value));
        }
        return _DateTime;
    },
    addDays: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date(Date.parse(_DateTime) + (86400000 * value));
        }
        return _DateTime;
    },
    addHours: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date(Date.parse(_DateTime) + (3600000 * value));
        }
        return _DateTime;
    },
    addMinutes: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date(Date.parse(_DateTime) + (60000 * value));
        }
        return _DateTime;
    },
    addSeconds: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date(Date.parse(_DateTime) + (1000 * value));
        }
        return _DateTime;
    },
    addMilliseconds: function (_DateTime, value) {
        if (Object.prototype.toString.apply(_DateTime) == "[object Date]" && Object.prototype.toString.apply(value) == "[object Number]") {
            _DateTime = new Date(Date.parse(_DateTime) + value);
        }
        return _DateTime;
    },
    toString: function (_DateTime, format) {
        var result = null;
        if (!ObjectUtils.hasValue(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        if (format == "UTCDateTime") {
            ///  /Date(1311821221173+0800)/
            result = "/Date(" + (TL.DateTime.AddHours(_DateTime, -8) - TL.ToDateTime("1970-01-01")) + "+0800)/";
        } else {
            var o = {
                "M+": _DateTime.getMonth() + 1, //月份
                "d+": _DateTime.getDate(), //日
                "h+": _DateTime.getHours() % 12 == 0 ? 12 : _DateTime.getHours() % 12, //12小时
                "H+": _DateTime.getHours(), //24小时
                "m+": _DateTime.getMinutes(), //分
                "s+": _DateTime.getSeconds(), //秒
                "q+": Math.floor((_DateTime.getMonth() + 3) / 3), //季度
                "S": _DateTime.getMilliseconds() //毫秒
            };
            var week = {
                "0": "/u65e5",
                "1": "/u4e00",
                "2": "/u4e8c",
                "3": "/u4e09",
                "4": "/u56db",
                "5": "/u4e94",
                "6": "/u516d"
            };
            if (/(y+)/.test(format)) {
                format = format.replace(RegExp.$1, (_DateTime.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            if (/(E+)/.test(format)) {
                format = format.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(format)) {
                    format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            result = format;
        }
        return result;
    },
    toInTime: function (startTime, endTime) {
        var seconds = Math.abs(parseInt((endTime - startTime) / 1000, 10));
        return fw.fwNumber.FWNumberHelper.toInTime(seconds);
    },
    toUTCDateTime: function (_DateTime) {
        return _DateTime - StringUtils.toDate("1970-01-01");
    }
};

export {
    RandomUtils,
    ObjectUtils,
    DateUtils
};