<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.dictName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="编码">
              <a-input placeholder="请输入编码" v-model="queryParam.dictCode"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="父级编码">
              <a-input placeholder="请输入父级编码" v-model="queryParam.parentCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="字典类型编码">
              <a-input placeholder="请输入字典类型编码" v-model="queryParam.dictTypeCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="拼音">
              <a-input placeholder="请输入拼音" v-model="queryParam.pinyin"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('字典')">导出</a-button>
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
    <sysDict-modal ref="modalForm" @ok="modalFormOk"></sysDict-modal>
  </a-card>
</template>

<script>
  import SysDictModal from './modules/SysDictModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "SysDictList",
    mixins:[JeecgListMixin],
    components: {
      SysDictModal
    },
    data () {
      return {
        description: '字典管理页面',
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
            title: '名称',
            align:"center",
            dataIndex: 'dictName'
           },
		   {
            title: '编码',
            align:"center",
            dataIndex: 'dictCode'
           },
		   {
            title: '父级编码',
            align:"center",
            dataIndex: 'parentCode'
           },
		   {
            title: '字典类型编码',
            align:"center",
            dataIndex: 'dictTypeCode'
           },
		   {
            title: '拼音',
            align:"center",
            dataIndex: 'pinyin'
           },
		   {
            title: '等级',
            align:"center",
            dataIndex: 'level'
           },
		   {
            title: '是否末级',
            align:"center",
            dataIndex: 'isEndNode'
           },
		   {
            title: '全编码',
            align:"center",
            dataIndex: 'fullCode'
           },
		   {
            title: '全名称',
            align:"center",
            dataIndex: 'fullName'
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
          list: "/dict/sysDict/list",
          delete: "/dict/sysDict/delete",
          deleteBatch: "/dict/sysDict/deleteBatch",
          exportXlsUrl: "dict/sysDict/exportXls",
          importExcelUrl: "dict/sysDict/importExcel",
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