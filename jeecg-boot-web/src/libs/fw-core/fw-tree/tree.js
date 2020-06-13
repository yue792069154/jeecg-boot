import _ from 'lodash';

const maxDepth = 9223372036854776000;

var CollectionUtils = {
    hasValue: function (arr) {
        arr = arr || [];
        return arr.length > 0;
    },
    addRange: function (originArr, itemArray) {
        itemArray = itemArray || [];
        for (var i = 0; i < itemArray.length; i++) {
            originArr.push(itemArray[i]);
        }
        return originArr;
    },
    contains: function (arr, item) {
        return arr.indexOf(item) > -1;
    }
};

export default class TreeImpl {

    constructor(data, idFieldName, parentIdFieldName, nodesFieldName) {

        this.treeList = [];
        this.nodeMap = {};

        this.idFieldName = idFieldName;
        this.parentIdFieldName = parentIdFieldName;
        this.nodesFieldName = nodesFieldName;

        if (_.isArray(data)) {
            this.treeList = data;
        } else {
            this.treeList = [data];
        }

        this.treeList = _.clone(this.treeList);

        this.init();
    }

    init() {

        var treeList = this.treeList;
        var nodeMap = this.nodeMap;
        var idFieldName = this.idFieldName;
        var parentIdFieldName = this.parentIdFieldName;
        var nodesFieldName = this.nodesFieldName;

        if (!_.isNil(treeList) && !_.isNil(nodesFieldName)) {

            //将数据平行化
            var nodeList = this._getList(treeList);
            //清空子节点
            this._clearChildNodes(nodeList);

            //清空所有平行华数据的子集，并将数据存入map
            var node;
            var id;
            for (var i = 0; i < nodeList.length; i++) {
                node = nodeList[i];
                id = node[idFieldName];
                nodeMap[id] = node;
            };

            //重新将数据树形化
            treeList = [];
            var parentNode;
            for (var i = 0; i < nodeList.length; i++) {
                node = nodeList[i];
                parentNode = this.getParentNode(node);
                if (!_.isNil(parentNode)) {
                    //有父级表示该节点是叶子
                    if (_.isNil(parentNode[nodesFieldName])) {
                        parentNode[nodesFieldName] = [];
                    }
                    //添加节点到父级
                    parentNode[nodesFieldName].push(node);
                } else {
                    //没父级表示该节点是根节点
                    treeList.push(node);
                }
            }
            this.treeList = treeList;

        }
    }

    _getList(treeList) {
        if (_.isNil(treeList)) {
            return treeList;
        };

        var allNodes = [];
        var allChildNodes;
        var node;
        for (var i = 0; i < treeList.length; i++) {
            node = treeList[i];
            allNodes.push(node);
            allChildNodes = this._getAllChildNodes(node);
            if (!_.isNil(allChildNodes)) {
                CollectionUtils.addRange(allNodes, allChildNodes);
            }
        }
        return allNodes;
    }

    _clearChildNodes(nodeList) {
        var nodesFieldName = this.nodesFieldName;

        if (!_.isArray(nodeList)) {
            nodeList = [nodeList];
        };
        var node;
        for (var i = 0; i < nodeList.length; i++) {
            node = nodeList[i];
            node[nodesFieldName] = [];
        }
    }

    _getChildNodes(node) {
        var nodesFieldName = this.nodesFieldName;

        var childNodes = node[nodesFieldName];
        if (_.isNil(childNodes)) {
            childNodes = [];
        }
        return childNodes;
    }

    _getAllChildNodes(node, depth) {
        if (null == node) {
            return null;
        }

        if (null == depth) {
            //如果depth==null 将树的查询深度设置为最大值
            depth = maxDepth;
        }

        if (depth < 1) {
            return null;
        }

        depth--;

        var allChildNodes = [];
        var childNodes = this._getChildNodes(node);
        var childAllChildNodes;
        var childNode;
        for (var i = 0; i < childNodes.length; i++) {
            childNode = childNodes[i];
            allChildNodes.push(childNode);
            childAllChildNodes = this._getAllChildNodes(childNode, depth);
            if (!_.isNil(childAllChildNodes)) {
                CollectionUtils.addRange(allChildNodes, childAllChildNodes);
            }
        }
        return allChildNodes;
    }

    getAncestors(param0) {
        //参数param0 两种类型 node || idFieldValue

        var node = param0;
        if (_.isString(param0)) {
            var idFieldValue = param0;
            node = this.getNode(idFieldValue);
        };

        var ancestors = [];
        var parentNode = null;
        do {
            parentNode = this.getParentNode(node);
            if (!_.isNil(parentNode)) {
                ancestors.unshift(parentNode);
                node = parentNode;
            }
        } while (!_.isNil(parentNode));
        return ancestors;
    }

    getParentNode(param0) {
        //参数param0 两种类型 node || idFieldValue

        var parentIdFieldName = this.parentIdFieldName;

        var node = param0;
        if (_.isString(param0)) {
            var idFieldValue = param0;
            node = this.getNode(idFieldValue);
        };

        var parentNode = null;
        var parentId = node[parentIdFieldName];
        parentNode = this.getNode(parentId);

        return parentNode;
    }

    getNode(idFieldValue) {
        return this.nodeMap[idFieldValue];
    }

    getFirstLeafNode() {
        var nodesFieldName = this.nodesFieldName;

        var node = null;

        var childNodes = this.treeList;
        while (childNodes.length > 0) {
            node = childNodes[0];
            childNodes = node[nodesFieldName];
        };

        return node;
    }

    getList(depth) {
        if (!_.isNil(depth)) {
            depth = maxDepth;
        };

        var treeList = this.getTreeList(depth);
        treeList = this._getList(treeList);
        this._clearChildNodes(treeList);
        return treeList;
    }

    _processTreeList(treeList, depth) {
        if (!_.isNil(treeList)) {
            return;
        }

        if (!_.isNil(depth)) {
            //如果depth==null 将树的查询深度设置为最大值
            depth = maxDepth;
        };

        if (depth < 1) {
            treeList.splice(0, treeList.length);
            return;
        }

        depth--;

        var childNodes;
        var node;
        for (var i = 0; i < treeList.length; i++) {
            node = treeList[i];
            childNodes = this._getChildNodes(node);
            this._processTreeList(childNodes, depth);
        }
    }

    getTreeList(depth) {
        var treeList = this.treeList;

        if (!_.isNil(depth)) {
            //如果depth==null 将树的查询深度设置为最大值
            depth = maxDepth;
        };

        var objList = null;

        if (!_.isNil(treeList)) {
            objList = _.clone(treeList);
            this._processTreeList(objList, depth);
        } else {
            objList = [];
        }

        return objList;
    }

    getTreeListByParentIdFieldValue(parentIdFieldValue, depth) {
        var nodesFieldName = this.nodesFieldName;

        var objList = null;

        if (!_.isNil(depth)) {
            //如果depth==null 将树的查询深度设置为最大值
            depth = maxDepth;
        };

        var node = this.getNode(parentIdFieldValue);
        if (!_.isNil(node)) {
            var childNodes = node[nodesFieldName];
            if (!_.isNil(childNodes)) {
                if (childNodes.length > 0) {
                    objList = _.clone(childNodes);
                    this._processTreeList(objList, depth);
                } else {
                    objList = [];
                }
            }
        }

        return objList;
    }

    getTreeByIdFieldValue(idFieldValue, depth) {
        var obj = null;

        if (!_.isNil(depth)) {
            //如果depth==null 将树的查询深度设置为最大值
            depth = maxDepth;
        };

        var node = this.getNode(idFieldValue);
        if (!_.isNil(node)) {
            obj = _.clone(node);
            var objList = [obj];
            this._processTreeList(objList, depth);
            if (!_.isNil(objList)) {
                obj = objList[0];
            } else {
                obj = null;
            }
        }

        return obj;
    }

    getFullIdList(param0) {
        var idFieldName = this.idFieldName;
        //参数param0 两种类型 node || idFieldValue

        var node = param0;
        if (_.isString(param0)) {
            var idFieldValue = param0;
            node = this.getNode(idFieldValue);
        };

        var ancestors = this.getAncestors(param0);
        ancestors.push(node);

        var fullIdList = [];
        for (var i = 0; i < ancestors.length; i++) {
            fullIdList.push(ancestors[i][idFieldName]);
        };
        return fullIdList;
    }

    isLeaf(param0) {
        var idFieldName = this.idFieldName;
        var nodesFieldName = this.nodesFieldName;
        //参数param0 两种类型 node || idFieldValue

        var node = param0;
        if (_.isString(param0)) {
            var idFieldValue = param0;
            node = this.getNode(idFieldValue);
        };

        return node[nodesFieldName].length == 0;
    }

    getLevel(param0) {
        var idFieldName = this.idFieldName;
        var nodesFieldName = this.nodesFieldName;
        //参数param0 两种类型 node || idFieldValue

        var node = param0;
        if (_.isString(param0)) {
            var idFieldValue = param0;
            node = this.getNode(idFieldValue);
        };

        var ancestors = this.getAncestors(param0);

        return ancestors.length + 1;
    }

}