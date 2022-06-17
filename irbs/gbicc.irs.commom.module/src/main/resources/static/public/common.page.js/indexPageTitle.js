
isc.FwLogo.addProperties({
	initWidget: function () {
		
		var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    var strHour = date.getHours();
	    var strMinutes = date.getMinutes();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
		if (strHour >= 0 && strHour <= 9) {
		        strHour = "0" + strHour;
		    }
		if (strMinutes >= 0 && strMinutes <= 9) {
		        strMinutes = "0" + strMinutes;
		}
	
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + strHour + seperator2 + strMinutes;
			
        this.Super("initWidget", arguments);
        this.addMembers([
 			isc.Img.create({
 				width	: this.logo.width,
 				height	: this.logo.height,
 				src		: this.logo.url
 			}),
 			isc.LayoutSpacer.create({width:10}),
 			isc.Label.create({
 				width	:'100%',
 				height	: this.logo.height,
 				styleName:'fr-application-title',
 				contents: FrameworkUiInterface.applicationTitle
 			}),
 			isc.Label.create({
 				ID:'logout_href',
 				width: 40,
 				height:this.logo.height,
 				contents:'<a href="'+FrameworkUiInterface.webContextPath+'/logout" style="color:white;font-size:12px;">退出</a>'
 			}),
 			isc.Label.create({
 				ID:'clock',
 				width: 180,
 				height:this.logo.height,
 				contents:'<div style="color:white;font-size:12px;"><b>登录时间：'+currentdate+'</b></div>'
 			})
 		]);
		var rightIndex =5;
		if(this.data){
			for (var i=0; i < this.data.length; i++) {
				var menu =this.data[i];
				
				var menuButton =null;
				if(menu.menuType=='CATEGORY'){//如果是分类，表示存在子菜单
					menuButton =isc.ToolStripMenuButton.create({
						height	: this.logo.height,
						title 	: menu.title,
						menu	: isc.Menu.create({
						    showShadow: true,
						    shadowDepth: 10,
						    data: menu.submenu,
						    target: this,
						 	itemClick: function(item){
						 		this.target.menuItemClicked(item);
						 	}
						})
					});
				}else{//如果不是分类，表示是直接的菜单
					menuButton =isc.ToolStripButton.create({
					    icon: menu.icon,
					    title: menu.title,
					    target: this,
					    menuItem: menu,
					    action: function(){
					    	this.target.menuItemClicked(this.menuItem);
					    }
					});
				}
				if(menu.align=='RIGHT'){
					this.addMember(menuButton,rightIndex);
					rightIndex++;
					
					//添加空白
					this.addMember(isc.LayoutSpacer.create({width:5}),rightIndex);
					rightIndex++;
				}
			}
			this.addMember(isc.LayoutSpacer.create({width:5}));
		}
    }
});

