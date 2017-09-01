/*jslint nomen:true */
(function (global, pluginId) {
	"use strict";
	var Ember = global.Ember,
		$ = global.jQuery,
		// Cleanly parse the content of the URL query
		queryArgs = (function () {
			var ret = {};
			global.location.search.replace(/^\?/, '').split('&').forEach(function (pair) {
				pair = pair.split('=');
				ret[global.unescape(pair.shift())] = global.unescape(pair.join('='));
			});
			return ret;
		}()),	 
		// Pattern for updating the address bar
		containerUriPattern = '/rsuite-cms/plugin/' + pluginId + '/editor/applet.html?skey=%@2&rsuiteId=%@1',
		// Pattern for retrieving a MO's metadata
		metadataUriPattern = '/rsuite/rest/v2/content/id/%@1?skey=%@2',
		// Oxygen Ember application
		Oxygen = global.Oxygen = Ember.Application.extend({
			sessionKey: $('meta[data-session-key]').data('session-key'),
			managedObjectId: null,
			// The actual applet element, used for API access
			api: null,
			// On completion of init, the Oxygen applet will set this to true
			appletReady: false,
			// Add a document to the applet
			addDocument: function (moId) {
				var api = this.get('api'),
					skey = this.get('sessionKey'),
					def = new $.Deferred();
				if (api && api.openDocumentInNewTab && skey && moId) {
					//We don't know the details, but the JNLP should; download a copy and parse the parameters out
					api.openDocumentInNewTab(moId);
					def.resolve();
				} else {
					def.reject("oXygen Applet is not ready");
				}
				return def;
			},
			dirty: false,
			checkDirty: function (event) {
				if (this.get('dirty')) {
					return "There are some unsaved changes. Are you sure you want to close the editor?";
				}
			},
			//As soon as the applet is loaded, request permission from the user to make Javascript-to-Java calls.
			appletBecameReady: Ember.observer(function () {
				console.log('Applet is ready!');
				$('applet.oxygen-editor').get(0).getSecurityPermissions();
				this.notifyPropertyChange('managedObjectId');
			}, 'appletReady'),
			title: 'RSuite Oxygen Integration',
			titleChanged: Ember.observer(function () {
				document.title = (this.get('dirty') ? '* ' : '') + this.get('title');
			}, 'title', 'dirty'),
			managedObjectIdChanged: Ember.observer(function () {
				var skey = this.get('sessionKey'),
					moId = this.get('managedObjectId'),
					inst = this;
				if (skey && moId) {
					$.ajax({
						url: metadataUriPattern.fmt(moId, skey),
						headers: { Accept: 'application/json' }
					})
						.done(function (moData) {
							var title = moData.value.label || moData.value.managedObject.displayName;
							inst.set('title', title);
						});
				}
			}, 'managedObjectId'),
			activeDocument: null,
			activeDocumentChanged: Ember.observer(function () {
				var moId = (this.get('activeDocument').match(/\/content\/([^\?]+)/)||[])[1];
				this.set('managedObjectId', moId);
			}, 'activeDocument'),
			// Called by the applet when there is nothing else to present.
			close: function () {
				//global.window.close();
				console.log('Window close requested');
			},
			init: function () {
				this.titleChanged();
				// Trap an exit attempt
				$(global.window).on('beforeunload', function (event) {
					return Oxygen.checkDirty(event);
				});
				$(global.window).on('load', function () {
					Oxygen.set('api', $('applet.oxygen-editor').get(0));		
				});
			}
		}).create();
	
}(this, 'rsuite-oxygen-applet-integration'));