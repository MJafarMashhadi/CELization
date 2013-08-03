var DISQUS=function(g){var e=g.DISQUS||{};e.AssertionError=function(b){this.message=b};e.AssertionError.prototype.toString=function(){return"Assertion Error: "+(this.message||"[no message]")};e.assert=function(b,c,f){if(!b)if(f)g.console&&g.console.log("DISQUS assertion failed: "+c);else throw new e.AssertionError(c);};var c=[];e.define=function(b,h){typeof b==="function"&&(h=b,b="");for(var f=b.split("."),a=f.shift(),d=e,o=(h||function(){return{}}).call({overwrites:function(a){a.__overwrites__=!0;
return a}},g);a;)d=d[a]?d[a]:d[a]={},a=f.shift();for(var j in o)o.hasOwnProperty(j)&&(!o.__overwrites__&&d[j]!==null&&e.assert(!d.hasOwnProperty(j),"Unsafe attempt to redefine existing module: "+j,!0),d[j]=o[j],c.push(function(a,d){return function(){delete a[d]}}(d,j)));return d};e.use=function(b){return e.define(b)};e.cleanup=function(){for(var b=0;b<c.length;b++)c[b]()};return e}(window);
DISQUS.define(function(g,e){var c=g.DISQUS,b=g.document,h=b.getElementsByTagName("head")[0]||b.body,f={running:!1,timer:null,queue:[]};c.defer=function(a,d){function b(){var a=f.queue;if(a.length===0)f.running=!1,clearInterval(f.timer);for(var d=0,c;c=a[d];d++)c[0]()&&(a.splice(d--,1),c[1]())}f.queue.push([a,d]);b();if(!f.running)f.running=!0,f.timer=setInterval(b,100)};c.each=function(a,d){var b=a.length,c=Array.prototype.forEach;if(isNaN(b))for(var h in a)a.hasOwnProperty(h)&&d(a[h],h,a);else if(c)c.call(a,
d);else for(c=0;c<b;c++)d(a[c],c,a)};c.extend=function(a){c.each(Array.prototype.slice.call(arguments,1),function(d){for(var b in d)a[b]=d[b]});return a};c.serializeArgs=function(a){var d=[];c.each(a,function(a,b){a!==e&&d.push(b+(a!==null?"="+encodeURIComponent(a):""))});return d.join("&")};c.isString=function(a){return Object.prototype.toString.call(a)==="[object String]"};c.serialize=function(a,d,b){d&&(a+=~a.indexOf("?")?a.charAt(a.length-1)=="&"?"":"&":"?",a+=c.serializeArgs(d));if(b)return d=
{},d[(new Date).getTime()]=null,c.serialize(a,d);d=a.length;return a.charAt(d-1)=="&"?a.slice(0,d-1):a};c.require=function(a,d,f,e,l){function g(a){if(a.type=="load"||/^(complete|loaded)$/.test(a.target.readyState))e&&e(),q&&clearTimeout(q),c.bean.remove(a.target,p,g)}var k=b.createElement("script"),p=k.addEventListener?"load":"readystatechange",q=null;k.src=c.serialize(a,d,f);k.async=!0;k.charset="UTF-8";(e||l)&&c.bean.add(k,p,g);l&&(q=setTimeout(function(){l()},2E4));h.appendChild(k);return c};
c.requireStylesheet=function(a,d,f){var e=b.createElement("link");e.rel="stylesheet";e.type="text/css";e.href=c.serialize(a,d,f);h.appendChild(e);return c};c.requireSet=function(a,d,b){var h=a.length;c.each(a,function(a){c.require(a,{},d,function(){--h===0&&b()})})};c.injectCss=function(a){var d=b.createElement("style");d.setAttribute("type","text/css");a=a.replace(/\}/g,"}\n");g.location.href.match(/^https/)&&(a=a.replace(/http:\/\//g,"https://"));d.styleSheet?d.styleSheet.cssText=a:d.appendChild(b.createTextNode(a));
h.appendChild(d)}});
DISQUS.define("JSON",function(){function g(a){return a<10?"0"+a:a}function e(b){a.lastIndex=0;return a.test(b)?'"'+b.replace(a,function(a){var b=j[a];return typeof b==="string"?b:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+b+'"'}function c(a,b){var f,g,j,m,k=d,n,i=b[a];i&&typeof i==="object"&&typeof i.toJSON==="function"&&!h&&(i=i.toJSON(a));typeof l==="function"&&(i=l.call(b,a,i));switch(typeof i){case "string":return e(i);case "number":return isFinite(i)?String(i):"null";case "boolean":case "null":return String(i);
case "object":if(!i)return"null";d+=o;n=[];if(Object.prototype.toString.apply(i)==="[object Array]"){m=i.length;for(f=0;f<m;f+=1)n[f]=c(f,i)||"null";j=n.length===0?"[]":d?"[\n"+d+n.join(",\n"+d)+"\n"+k+"]":"["+n.join(",")+"]";d=k;return j}if(l&&typeof l==="object"){m=l.length;for(f=0;f<m;f+=1)g=l[f],typeof g==="string"&&(j=c(g,i))&&n.push(e(g)+(d?": ":":")+j)}else for(g in i)Object.hasOwnProperty.call(i,g)&&(j=c(g,i))&&n.push(e(g)+(d?": ":":")+j);j=n.length===0?"{}":d?"{\n"+d+n.join(",\n"+d)+"\n"+
k+"}":"{"+n.join(",")+"}";d=k;return j}}var b={},h=!1;if(typeof Date.prototype.toJSON!=="function")Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+g(this.getUTCMonth()+1)+"-"+g(this.getUTCDate())+"T"+g(this.getUTCHours())+":"+g(this.getUTCMinutes())+":"+g(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()};var f=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
a=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,d,o,j={"\u0008":"\\b","\t":"\\t","\n":"\\n","\u000c":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},l;b.stringify=function(a,b,h){var f;o=d="";if(typeof h==="number")for(f=0;f<h;f+=1)o+=" ";else typeof h==="string"&&(o=h);if((l=b)&&typeof b!=="function"&&(typeof b!=="object"||typeof b.length!=="number"))throw Error("JSON.stringify");return c("",{"":a})};b.parse=function(a,b){function d(a,
c){var h,f,e=a[c];if(e&&typeof e==="object")for(h in e)Object.hasOwnProperty.call(e,h)&&(f=d(e,h),f!==void 0?e[h]=f:delete e[h]);return b.call(a,c,e)}var c,a=String(a);f.lastIndex=0;f.test(a)&&(a=a.replace(f,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return c=eval("("+a+")"),
typeof b==="function"?d({"":c},""):c;throw new SyntaxError("JSON.parse");};var m={a:[1,2,3]},k,p;if(Object.toJSON&&Object.toJSON(m).replace(/\s/g,"")==='{"a":[1,2,3]}')k=Object.toJSON;typeof String.prototype.evalJSON==="function"&&(m='{"a":[1,2,3]}'.evalJSON(),m.a&&m.a.length===3&&m.a[2]===3&&(p=function(a){return a.evalJSON()}));(function(){var a=[1,2,3];typeof a.toJSON==="function"&&(a=a.toJSON(),h=!(a&&a.length===3&&a[2]===3))})();if(h||!k||!p)return{stringify:b.stringify,parse:b.parse};return{stringify:k,
parse:p}});
DISQUS.define("Bus",function(){function g(a){a=a.split("/");return a[0]+"//"+a[2]}var e=DISQUS.use("JSON"),c=window.location.hash.slice(1),b=window.parent,h=document.referrer,f={};f.client=g(document.location.href);f.host=h?g(h):f.client;return{origins:f,messageHandler:function(a){var b=a.origin;if(!(b!==f.client&&b!==f.host)){var c;try{c=DISQUS.JSON.parse(a.data)}catch(h){return}a=c.data;if(!(a.eventName[0]==="!"&&b!==f.client))switch(c.scope){case "client":DISQUS.Bus.trigger(a.eventName,a.data)}}},
postMessage:function(a){a.sender=c;a=e.stringify(a);b.postMessage(a,f.host)},sendHostMessage:function(a,b){b=b||[];DISQUS.Bus.postMessage({scope:"host",name:a,data:b})},sendGlobalMessage:function(a,b){b=b||[];DISQUS.Bus.postMessage({scope:"global",name:a,data:b})}}});_.extend(DISQUS.Bus,Backbone.Events);
$(document).ready(function(){var g=window.addEventListener?window.addEventListener:window.attachEvent,e=window.addEventListener?"message":"onmessage";if(!g)throw Error("No event support.");g(e,DISQUS.Bus.messageHandler,!1)});
DISQUS.define(function(g,e){var c={blocks:{},ISO_8601:"YYYY-MM-DDTHH:mm:ssZ",apiKey:"E8Uh5l5fHZ6gD8U3KycjAIAk46f68Zw7C6eW8WSjZvCLXebZ7p0r1yrYDrLilk2F",bean:require("bean"),debug:!1,browser:{mobile:navigator.userAgent.match(/Android|webOS|iPhone|iPad|iPod|BlackBerry/i)},modules:{},strings:{translations:{}}};c.strings.get=_.bind(function(b){var c=this.translations[b];return c!==e?c:b},c.strings);c.interpolate=function(b,c){return b.replace(/%\(\w+\)s/g,function(a){var a=a.slice(2,-2),d="";a in c?d=
c[a]!==e&&c[a]!==null?c[a].toString():"":DISQUS.logError&&DISQUS.logError("Key `"+a+"` not found in context for: ",b);return d})};c.addBlocks=function(){return function(b){b(DISQUS)}};c.templateGlobals={urls:DISQUS.use("urls"),sso:null,gettext:c.strings.get};c.renderBlock=function(b,f){return DISQUS.blocks[b](c.templateGlobals,f)};c.assureOffset=function(b){return b.indexOf("+")>=0?b:b+"+00:00"};var b=c.Builder=function(){this.accum=[]};_.extend(b.prototype,{put:function(b){this.accum.push(b)},esc:function(b){return String(b).replace(/&/g,
"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;").replace(/'/g,"&#039;")},compile:function(){return this.accum.join("")}});return c});
DISQUS.define("urls",function(){var g=DISQUS.debug?"/js/src/embed/next/vglnk.js":"/build/next/alfie.js",e=encodeURIComponent(document.referrer),c={root:"http://disqus.com",media:"http://mediacdn.disqus.com/1364427432",translations:"http://mediacdn.disqus.com/1364427432/build/lang",loading:"http://mediacdn.disqus.com/1364427432/html/simple-loading.html",realertime:"",login:"https://disqus.com/next/login/",api:"http://disqus.com/api/3.0/",apiSecure:"https://disqus.com/api/3.0/",logout:"http://disqus.com/logout/?redirect="+
e,editProfile:"http://disqus.com/account/",verifyEmail:"https://disqus.com/next/verify/",authorize:"https://disqus.com/api/oauth/2.0/authorize/",moderate:"http://disqus.com/admin/moderate/",oauth:{twitter:"http://disqus.com/_ax/twitter/begin/",google:"http://disqus.com/_ax/google/begin/",facebook:"http://disqus.com/_ax/facebook/begin/"},avatar:{generic:"http://mediacdn.disqus.com/1364427432/img/next/noavatar92.png"},linkAffiliatorClient:"http://mediacdn.disqus.com/1364427432"+g};window.location.protocol===
"https:"&&(c=_.extend(c,{root:"",media:"",translations:"https://securecdn.disqus.com/1364427432/build/lang",loading:"/html/simple-loading.html",api:c.apiSecure,logout:"https://disqus.com/logout/?redirect="+e,editProfile:"https://disqus.com/account/",moderate:"https://disqus.com/admin/moderate/",oauth:{twitter:"https://disqus.com/_ax/twitter/begin/",google:"https://disqus.com/_ax/google/begin/",facebook:"http://disqus.com/_ax/facebook/begin/"},avatar:{generic:"/img/next/noavatar92.png"},linkAffiliatorClient:""+
g}));return c});
DISQUS.define("api",function(){function g(b){function c(a){var e=a.originalEvent.origin;DISQUS.urls.apiSecure.slice(0,e.length)===e&&(a=DISQUS.JSON.parse(a.originalEvent.data),a.requestId===f&&(e=a.code===0?b.success:b.error,delete a.requestId,(e||function(){})(a),document.body.removeChild(g),document.body.removeChild(d),$(window).off("message",c)))}var f=_.uniqueId("ft_"),a=document.createElement("div"),d=document.createElement("form"),e="frame_"+f,g;a.innerHTML='<iframe name="'+e+'"></iframe>';
g=a.childNodes[0];d.target=e;d.action=b.url.replace(".json",".pm");d.method=b.method||"GET";b.data=_.extend(b.data,{callback:f,referrer:document.referrer});_.each(b.data,function(a,b){a===!0?a=[1]:a===!1?a=[0]:a===null?a=[""]:_.isArray(a)||(a=[a]);_.each(a,function(a){var c=document.createElement("input");c.type="hidden";c.name=b;c.value=a;d.appendChild(c)})});$(window).on("message",c);document.body.appendChild(g);document.body.appendChild(d);d.submit()}function e(b){b=_.defaults(b,c);b.data=b.data||
{};b.data.api_key=DISQUS.apiKey;return $.ajax(b)}var c={};return{defaults:function(b){var e,f,a;for(e in b)f=b[e],a=c[e],_.isObject(f)&&_.isObject(a)?_.extend(a,f):c[e]=f},headers:function(b){b=_.extend({},c.headers,b);return c.headers=_.pick(b,_.map(b,function(b,c){if(b)return c}))},getURL:function(b,c){c=c||{};if(c.secure||window.location.protocol==="https:")return DISQUS.urls.apiSecure+b;return DISQUS.urls.api+b},ajax:e,call:function(b,c){c=c||{};c.url=DISQUS.api.getURL(b,{secure:c.secure});c.data=
_.extend(c.data||{},{api_key:DISQUS.apiKey});return(c.secure?g:e)(c)}}});(function(g){var e=DISQUS.Bus,c=DISQUS.api.getURL("internal/switches/list.jsonp");g.switches_jsonp_cb=function(b){e.sendHostMessage("switches.received",b.response)};e.on("fetch",function(b){b=b||{};b.data=b.data||{};b.data.callback="switches_jsonp_cb";b.data.api_key=DISQUS.apiKey;b=DISQUS.serialize(c,b.data,!1);DISQUS.require(b)});e.sendHostMessage("ready")})(this);