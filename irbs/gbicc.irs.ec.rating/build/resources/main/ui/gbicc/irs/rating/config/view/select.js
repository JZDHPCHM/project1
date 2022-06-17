/**
 * ==============================================================================================================================
 * 初始化操作
 * ==============================================================================================================================
 */
//将菜单栏的高度设置为0（即不显示菜单栏）
EditorUi.prototype.menubarHeight = 0;
//设置 sliderbar 宽度
EditorUi.prototype.hsplitPosition = (screen.width <= 640) ? 118 : 150;
//覆盖保存方法
EditorUi.prototype.saveFile = function(forceDialog){
	var xml =mxUtils.getPrettyXml(this.editor.getGraphXml());
	$.post(window.mxSaveXmlUrl,{xml:xml},function(data){
		$.tips('保存成功！',2000);
	});
}

/**
 * ==============================================================================================================================
 * 模型列表加载器
 * ==============================================================================================================================
 */
ModelLoader =function(){
	this.list =[];
}
ModelLoader.prototype.load =function(){
	var xhr =mxUtils.load("[(#{/irs/modelSelector/getJsonModelList})]");
	this.list =JSON.parse(xhr.getText());
}
ModelLoader.prototype.getList =function(){
	return this.list;
}
ModelLoader.prototype.getNameByCode =function(code){
	if(this.list && this.list.length>0){
		for(var i=0;i<this.list.length;i++){
			if(this.list[i].code ==code){
				return this.list[i].name;
			}
		}
	}
}
var modelLoader =new ModelLoader();
//初始化之后
EditorUi.prototype.afterCreated =function(){
	//防止连线未连接到节点上,即连线必须要连接两个节点
	this.editor.graph.setAllowDanglingEdges(false);
	this.editor.graph.setDisconnectOnMove(false);
	//设置 cell 不可编辑
	this.editor.graph.isCellEditable = function(cell){
		return false;
	}
	modelLoader.load();
}

/**
 * ==============================================================================================================================
 * 覆盖 sidebar 部分
 * ==============================================================================================================================
 */
Sidebar.prototype.init = function()
{
	this.addSearchPalette(true);
	this.addModelSelectPathPalette(true);
};

/**
 * 添加自定义组件模板
 */
Sidebar.prototype.addModelSelectPathPalette = function(expand)
{
	var fns = [
		this.addEntry('开始', mxUtils.bind(this, function()
	 	{
	 		var cell = new mxCell('开始', new mxGeometry(0, 0, 50, 50), 'ellipse;whiteSpace=wrap;html=1;aspect=fixed;comic=1;fillColor=#99CCFF;');
	 		cell.vertex = true;
	 		this.graph.setAttributeForCell(cell, 'type', 'Start');
	 		return this.createVertexTemplateFromCells([cell], cell.geometry.width, cell.geometry.height, '开始');
	 	})),
		this.addEntry('条件判断', mxUtils.bind(this, function()
	 	{
	 		var cell = new mxCell('条件判断', new mxGeometry(0, 0, 120, 60), 'rhombus;whiteSpace=wrap;html=1;comic=1;fillColor=#99FF99;');
	 		cell.vertex = true;
	 		this.graph.setAttributeForCell(cell, 'type', 'Condition');
	 		this.graph.setAttributeForCell(cell, 'condition', '');
	 		this.graph.setAttributeForCell(cell, 'expression', '');
	 		return this.createVertexTemplateFromCells([cell], cell.geometry.width, cell.geometry.height, '条件判断');
	 	})),
	 	this.addEntry('模型', mxUtils.bind(this, function()
	 	{
	 		var cell = new mxCell('模型', new mxGeometry(0, 0, 100, 30), 'rounded=1;whiteSpace=wrap;html=1;comic=1;fillColor=#9AC7BF;');
	 		cell.vertex = true;
	 		this.graph.setAttributeForCell(cell, 'type', 'Model');
	 		this.graph.setAttributeForCell(cell, 'code', '');
	 		this.graph.setAttributeForCell(cell, 'name', '');
	 		return this.createVertexTemplateFromCells([cell], cell.geometry.width, cell.geometry.height, '模型');
	 	}))
	];
	
	this.addPaletteFunctions('modelSelectPathPalette', '模型选择路径', (expand != null) ? expand : true, fns);
};

/**
 * ==============================================================================================================================
 * 覆盖 toolbar 部分
 * ==============================================================================================================================
 */
var toolbarInit =Toolbar.prototype.init;
Toolbar.prototype.init = function(){
	//添加保存按钮
	var saveItems = this.addItems(['save']);
	saveItems[0].setAttribute('title', mxResources.get('save') + ' (' + this.editorUi.actions.get('save').shortcut + ')');
	saveItems[0].setAttribute('iconCls','wsp');
	this.addSeparator();
	
	//添加默认工具条(父类)
	toolbarInit.apply(this, arguments);

	//添加编辑图表按钮
	this.addSeparator();
	var saveItems = this.addItems(['editDiagram']);
	saveItems[0].setAttribute('title', mxResources.get('editDiagram'));
}

/**
 * ==============================================================================================================================
 * 自定义Format面板显示的标签列表
 * ==============================================================================================================================
 */
var formatGetDiagramFormaters =Format.prototype.getDiagramFormaters;
//根据当前选择的节点显示格式化面板
Format.prototype.getDiagramFormaters =function(){
	var tabs =formatGetDiagramFormaters.apply(this, arguments);
	var graph = this.editorUi.editor.graph;
	var cells =graph.getSelectionCells();
	if(cells && cells.length>0){
		var type =cells[0].getAttribute('type');
		if(cells[0].isEdge() || type=='Condition' || type=='Model'){
			tabs.splice(0, 0, 'configure');
		}
	}
	return tabs;
}

//重写插入边方法，加入自定义对象
var insertEdge = mxConnectionHandler.prototype.insertEdge;
mxConnectionHandler.prototype.insertEdge = function(parent, id, value, source, target, style){
	var edge = mxUtils.createXmlDocument().createElement('UserObject');
	edge.setAttribute('type', 'Edge');
	edge.setAttribute('label', '');
	edge.setAttribute('value', '');
	edge.setAttribute('valueType', '');
	value = edge;
	//验证模型节点不能连接模型节点
	var sourceNodeType = source.getAttribute("type").toLowerCase();
	var targetNodeType = target.getAttribute("type").toLowerCase();
  	if(sourceNodeType == 'model' && targetNodeType == "model"){
  		$.alert("模型节点之间无法连接！");
  		//$.tips('模型节点之间无法连接！',2000);
  		return null;
  	}
  	return insertEdge.apply(this, arguments);
};


/**
 * ==============================================================================================================================
 * 自定义配置面板
 * ==============================================================================================================================
 */
ConfigureFormatPanel = function(format, editorUi, container)
{
	BaseFormatPanel.call(this, format, editorUi, container);
	this.init();
};

mxUtils.extend(ConfigureFormatPanel, BaseFormatPanel);

ConfigureFormatPanel.prototype.init = function()
{
	this.container.style.borderBottom = 'none';
	this.addUi(this.container);
};

ConfigureFormatPanel.prototype.addUi = function(container)
{
	var ui = this.editorUi;
	var editor = ui.editor;
	var graph = editor.graph;
	var ss = this.format.getSelectionState();
	
	var selectedCells =graph.getSelectionCells();
	if(selectedCells && selectedCells.length>0){
		var selectedCell =selectedCells[0];
		var formPanel =this.createFormPanel();
		container.appendChild(formPanel);
		
		if(selectedCell.isEdge()){
			this.createConnectionUi(graph,formPanel,selectedCell);
		}else if(mxUtils.isNode(selectedCell.value)){
			var type =selectedCell.getAttribute('type');
			if(type=='Model'){
				this.createModelNodeUi(graph,formPanel,selectedCell);
			}else if(type=='Condition'){
				this.createConditionNodeUi(graph,formPanel,selectedCell);
			}
		}
	}
	return container;
}

ConfigureFormatPanel.prototype.createModelNodeUi = function(graph,formPanel,selectedCell){
	var selectModelLabel =this.createLabel('请选择模型');
	formPanel.appendChild(selectModelLabel);
	
	var selectModelCombobox =this.createCombobox();
	formPanel.appendChild(selectModelCombobox);
	
	this.addOption(selectModelCombobox,'','');
	for (var i = 0; i < modelLoader.list.length; i++)
	{
		var model =modelLoader.list[i];
		this.addOption(selectModelCombobox,model.code,model.name);
	}
	
	//设置下拉框选择框的值
	selectModelCombobox.value =selectedCell.getAttribute('code');
	
	if (!graph.isEditing())
	{
		mxEvent.addListener(selectModelCombobox, 'change', mxUtils.bind(this,function(evt)
		{
			var code =selectModelCombobox.value;
			var name =modelLoader.getNameByCode(code);
			if(code){
				graph.getModel().beginUpdate();
				try
				{
					var elt = selectedCell.value.cloneNode(true);
				    elt.setAttribute('code', code);
				    elt.setAttribute('name', name);
				    elt.setAttribute('label', name);
				    graph.getModel().setValue(selectedCell, elt);
				}
				finally
				{
					graph.getModel().endUpdate();
				}
			}
			mxEvent.consume(evt);
		}));
	}
}

ConfigureFormatPanel.prototype.createConditionNodeUi = function(graph,formPanel,selectedCell){
	var expressionLabel =this.createLabel('表达式');
	formPanel.appendChild(expressionLabel);
	
	var expressionText =this.createText();
	expressionText.value =selectedCell.getAttribute('expression') || '';
	formPanel.appendChild(expressionText);
	
	formPanel.appendChild(document.createElement('br'));
	var expreesionHelpLabel =this.createHyperlink();
	formPanel.appendChild(expreesionHelpLabel);
	
	if (!graph.isEditing())
	{
		var applyHandler = function()
		{
			graph.getModel().beginUpdate();
			try
			{
				var elt = selectedCell.value.cloneNode(true);
			    elt.setAttribute('expression', expressionText.value);
			    graph.getModel().setValue(selectedCell, elt);
			}
			finally
			{
				graph.getModel().endUpdate();
			}
		};
		
		mxEvent.addListener(expressionText, 'keypress', function (evt)
		{
			if (evt.keyCode == /*enter*/13 && !mxEvent.isShiftDown(evt)){
				conditionText.blur();
			}
		});

		if (mxClient.IS_IE){
			mxEvent.addListener(expressionText, 'focusout', applyHandler);
		}else{
			mxEvent.addListener(expressionText, 'blur', applyHandler);
		}
	}
	
	var conditionLabel =this.createLabel('描述');
	formPanel.appendChild(conditionLabel);
	
	var conditionText =this.createText();
	conditionText.value =selectedCell.getAttribute('condition') || '';
	formPanel.appendChild(conditionText);
	
	if (!graph.isEditing())
	{
		var applyHandler = function()
		{
			graph.getModel().beginUpdate();
			try
			{
				var elt = selectedCell.value.cloneNode(true);
			    elt.setAttribute('condition', conditionText.value);
			    elt.setAttribute('label', conditionText.value);
			    graph.getModel().setValue(selectedCell, elt);
			}
			finally
			{
				graph.getModel().endUpdate();
			}
		};
		
		mxEvent.addListener(conditionText, 'keypress', function (evt)
		{
			if (evt.keyCode == /*enter*/13 && !mxEvent.isShiftDown(evt)){
				conditionText.blur();
			}
		});

		if (mxClient.IS_IE){
			mxEvent.addListener(conditionText, 'focusout', applyHandler);
		}else{
			mxEvent.addListener(conditionText, 'blur', applyHandler);
		}
	}
}

ConfigureFormatPanel.prototype.createConnectionUi = function(graph,formPanel,selectedCell){
	
	var connectionTypeLabel =this.createLabel('值类型');
	formPanel.appendChild(connectionTypeLabel);
	
	var valueTypeCombobox =this.createCombobox();
	this.addOption(valueTypeCombobox,'string','字符串');
	this.addOption(valueTypeCombobox,'number','数字');
	this.addOption(valueTypeCombobox,'boolean','布尔');
	valueTypeCombobox.value=selectedCell.getValue().getAttribute("valueType") || '';
	formPanel.appendChild(valueTypeCombobox);
	
	var connectionLabel =this.createLabel('条件值');
	formPanel.appendChild(connectionLabel);
	
	var valueText =this.createText();
	valueText.value =selectedCell.getValue().getAttribute("value") || '';
	formPanel.appendChild(valueText);
	
	if (!graph.isEditing())
	{
		var applyHandler = function()
		{
			graph.getModel().beginUpdate();
			try
			{
				var elt = selectedCell.value.cloneNode(true);
				var selIndex = valueTypeCombobox.selectedIndex;
			    elt.setAttribute('label', valueText.value);
			    elt.setAttribute('value', valueText.value);
			    elt.setAttribute('valueType', valueTypeCombobox.value);
			    graph.getModel().setValue(selectedCell, elt);
			}
			finally
			{
				graph.getModel().endUpdate();
			}
		};
		
		mxEvent.addListener(valueText, 'keypress', function (evt)
		{
			if (evt.keyCode == /*enter*/13 && !mxEvent.isShiftDown(evt)){
				valueText.blur();
			}
		});

		if (mxClient.IS_IE){
			mxEvent.addListener(valueTypeCombobox, 'focusout', applyHandler);
			mxEvent.addListener(valueText, 'focusout', applyHandler);
		}else{
			mxEvent.addListener(valueTypeCombobox, 'blur', applyHandler);
			mxEvent.addListener(valueText, 'focusout', applyHandler);
		}
	}
}

ConfigureFormatPanel.prototype.createFormPanel = function(){
	var formPanel = this.createPanel();
	formPanel.style.paddingTop = '2px';
	formPanel.style.paddingBottom = '2px';
	formPanel.style.position = 'relative';
	formPanel.style.marginLeft = '-2px';
	formPanel.style.borderWidth = '0px';
	formPanel.className = 'geToolbarContainer';
	
	if (mxClient.IS_QUIRKS){
		formPanel.style.display = 'block';
	}
	return formPanel;
}

ConfigureFormatPanel.prototype.createLabel = function(title){
	var label = this.createTitle(title);
	label.style.width = '100%';
	label.style.paddingTop = '10px';
	label.style.paddingBottom = '6px';
	return label;
}

ConfigureFormatPanel.prototype.createCombobox = function(){
	var result  =document.createElement('select');
	result.style.width = '210px';
	return result;
}

ConfigureFormatPanel.prototype.createHyperlink = function(){
	var link = document.createElement("a");
	link.href= "[(#{/html/AviatorHelp.html})]";
	link.target="_blank";
	link.innerText="*语法帮助";
	link.title="语法帮助";
	link.style.marginTop = '10px';
	link.style.width = '210px';
	return link;
}

ConfigureFormatPanel.prototype.addOption = function(combobox,value,text){
	var option = document.createElement('option');
	option.setAttribute('value', value);
	mxUtils.write(option, text);
	combobox.appendChild(option);
}

ConfigureFormatPanel.prototype.createText = function(){
	var result =document.createElement('input');
	result.type ='text';
	result.style.width = '210px';
	return result;
}