<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="字典类型名称">
              <a-input placeholder="请输入字典类型名称" v-model="queryParam.dictTypeName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="字典类型编码">
              <a-input placeholder="请输入字典类型编码" v-model="queryParam.dictTypeCode"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="扩展表名称">
              <a-input placeholder="请输入扩展表名称" v-model="queryParam.extendTableName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="扩展表中文名">
              <a-input placeholder="请输入扩展表中文名" v-model="queryParam.extendTableChineseName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="顺序">
              <a-input placeholder="请输入顺序" v-model="queryParam.sort"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('字典类型')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <sysDictType-modal ref="modalForm" @ok="modalFormOk"></sysDictType-modal>
  </a-card>
</template>

<script>
  import SysDictTypeModal from './modules/SysDictTypeModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "SysDictTypeList",
    mixins:[JeecgListMixin],
    components: {
      SysDictTypeModal
    },
    data () {
      return {
        description: '字典类型管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '字典类型名称',
            align:"center",
            dataIndex: 'dictTypeName'
           },
		   {
            title: '字典类型编码',
            align:"center",
            dataIndex: 'dictTypeCode'
           },
		   {
            title: '扩展表名称',
            align:"center",
            dataIndex: 'extendTableName'
           },
		   {
            title: '扩展表中文名',
            align:"center",
            dataIndex: 'extendTableChineseName'
           },
		   {
            title: '顺序',
            align:"center",
            dataIndex: 'sort'
           },
		   {
            title: '备注',
            align:"center",
            dataIndex: 'rem'
           },
		   {
            title: '状态',
            align:"center",
            dataIndex: 'statusCode'
           },
		   {
            title: '更新人',
            align:"center",
            dataIndex: 'updaterId'
           },
		   {
            title: '创建人',
            align:"center",
            dataIndex: 'createrId'
           },
		   {
            title: '创建时间',
            align:"center",
            dataIndex: 'creatTime'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/dict/sysDictType/list",
          delete: "/dict/sysDictType/delete",
          deleteBatch: "/dict/sysDictType/deleteBatch",
          exportXlsUrl: "dict/sysDictType/exportXls",
          importExcelUrl: "dict/sysDictType/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>