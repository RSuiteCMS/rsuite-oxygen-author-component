var dialogInstance;

function testFromJS(){
	if (dialogInstance){
		dialogInstance.dialogClose();
	}
}

(function (global, pluginId) {
	"use strict";
	var RSuite = global.RSuite,
		Ember = global.Ember,
		$ = global.jQuery;
	
	RSuite.createClass("RSuite.view.Dialog.OxygenLauncher", { extend: RSuite.View, mixin: [ RSuite.view.Dialog ] }, function (Self, log) {
		Self.reopen({
			classNames: 'test',
			templateName: RSuite.url(pluginId, 'launcher/templates/oxygen-launcher-template.hbr'),
			
			title: "Oxygen launcher",
			moId: '',
			dialogOpened: function () {				
			},
			dialogClosed: function () {
				this.get('$dialog').remove();
			},
			updateModel: function (moId) {
				var inst = this;
				inst.set('moId', moId);				
			},
			dialogOptions: {
				modal: false
			},
			attributeBindings: [ 'style' ],
			style: Ember.computed(function () {
				return 'max-height: ' + ((RSuite.document.height - 64) * 0.8) + 'px';
			}).property('RSuite.document.height'),
			
			skey: Ember.computed(function () { 
                return RSuite.model.session.get("key"); 
			}),
			
			jnlpUrl: Ember.computed(function (){
				var jnlpUrl = "/rsuite/rest/v1/api/rsuite.oxygen.launch.applet.jnlp?sourceId=" + this.moId + "&" + this.makeId();
				jnlpUrl += "&skey=" + RSuite.model.session.get("key");				
				return jnlpUrl;
			}),
			
			makeId: function ()
			{
			    var text = "";
			    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

			    for( var i=0; i < 5; i++ )
			        text += possible.charAt(Math.floor(Math.random() * possible.length));

			    return text;
			}
			
			
		});
	});
	
}(this, 'rsuite-oxygen-applet-integration'));