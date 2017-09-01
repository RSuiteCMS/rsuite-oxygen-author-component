(function (global, pluginId) {
	"use strict";
	var RSuite = global.RSuite,
		Ember = global.Ember,
		$ = global.jQuery,
		
		launchCondifurationUriPattern = '/rsuite/rest/v1/api/rsuite.launch.configuration?skey=%@1';	
		
	(function (nav) {
		/** 
		 * Populates jQuery.os with the following information:
		 * 	linux: boolean, whether the OS is linux-based
		 * 	osx: boolean, whether the OS is mac
		 * 	windows: boolean, whether the OS is windows
		 * 	bits: Enum(32, 64), bits for which the OS was compiled
		 * Also populates jQuery.browser.bits with:
		 *	bits: Enum(32, 64), bits for which the browser was compiled
		 */
		var ua = nav.userAgent,
			pl = nav.platform;
		$.os = {
			linux: (/^Linux/).test(pl),
			windows: (/^Win\d+/).test(pl),
			osx: (/^MacIntel/).test(pl),
			bits: (nav.cpuClass === 'x64' || (/x86_64|x86-64|Win64|x64;|amd64|AMD64|WOW64|x64_64/).test(ua)) ? 64 : 32
		};
		$.browser.bits = ((/Win64|x86_64/).test(pl) || !(/Google/).test(nav.vendor)) ? 64 : 32;
	}(global.navigator));
	
	
	
	RSuite.Action({
		id : "oxygen:launch",
		icon : RSuite.url(pluginId, 'action/oxygen-icon.png'),
		invoke : function (context) {
			
			var skey = RSuite.get('model.session.key');
			var launchMethod;
			
			$.ajax({
				async:   false,
				url: launchCondifurationUriPattern.fmt(skey)				 
			})
				.done(function (response) {
					launchMethod = $(response).find('configuration').attr('launchType');					
			});
			
			if ("launcher_applet" == launchMethod){
				return this.invokeLaucherApplet(context);
			}else{
				return this.invokeAppletInBrowserWindow(context);
			}						
			
			return RSuite.success;
		},
		
		launchOxygen : function (context, launchType) {
			console.log("INVOKE!!!");
		},
		
		invokeLaucherApplet : function (context){
			var moRef = Ember.get(context, 'managedObject.referenceManagedObject.id'), 			 
		 	moId = moRef == undefined ? Ember.get(context, 'managedObject.managedObject.id') : moRef;			
			if (moId){				
				var dlg = RSuite.view.Dialog.OxygenLauncher.create();
				dialogInstance = dlg;
				dlg.dialogShow();
				dlg.updateModel(moId);
				return RSuite.success;
			}else{
				global.console.error(context);
				return RSuite.failure;
			}
		},
		
		invokeAppletInBrowserWindow : function (context) {
			if ($.os.osx && $.browser.chrome && $.browser.bits === 32) {
			RSuite.Action('rsuite:messageDialog', {
				type: 'failure',
				title: 'Edit in Oxygen',
				message: "Google Chrome on OSX does not support Java 7; please use another browser to edit using oXygen"
			});
			return RSuite.failure;
			}
			
			var moRef = Ember.get(context, 'managedObject.referenceManagedObject.id'), 			 
		 	moId = moRef == undefined ? Ember.get(context, 'managedObject.managedObject.id') : moRef,
			moTitle = Ember.get(context, 'managedObject.finalManagedObject.displayName'),
			skey = RSuite.get('model.session.key'),
			moData = RSuite.Webservice.firstClassData(context).concat([ {name: 'appletContext', value: Ember.get(context, 'appletContext')} ]),
			popupUrl = RSuite.restUrl(1, 'api/rsuite.oxygen.applet') + '&' + moData.map(function (datum) {
				return global.escape(datum.name) + '=' + global.escape(datum.value);
			}).join('&'),
			popup = RSuite.openWindow({
				name: "oxygenAppletWindow",
				left: 0,
				top: 0,
				width: global.screen.availWidth,
				height: global.screen.availHeight,
				toolbar: false,
				menubar: false,
				location: false,
				status: false,
				scrollbars: false,
				resizable: true
			});			 	
		if (!moId) {
			global.console.error(context);
			return RSuite.failure;
		}
		if (popup.Oxygen) {
			return popup.Oxygen.addDocument(moId).fail(function (reason) {
				RSuite.Action('rsuite:messageDialog', {
					type: 'failure',
					title: 'Edit in Oxygen',
					message: reason || "Could not add document to running Oxygen instance"
				});
				popup.blur();
				global.window.focus();
			});
		}
		popup.location = popupUrl;
		return RSuite.success;
		}

	});
}(this, 'rsuite-oxygen-applet-integration'));