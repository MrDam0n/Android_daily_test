 How to use markdown in Sublime  3
================
1. Download Package Control  
    - Open console by using <b>ctrl</b>+<b>`</b> or View-->show console  
    - Input the following command, see detail in (https://packagecontrol.io/installation#st3)
```
import urllib.request,os,hashlib; h = '6f4c264a24d933ce70df5dedcf1dcaee' + 'ebe013ee18cced0ef93d5f746d80ef60'; pf = 'Package Control.sublime-package'; ipp = sublime.installed_packages_path(); urllib.request.install_opener( urllib.request.build_opener( urllib.request.ProxyHandler()) ); by = urllib.request.urlopen( 'http://packagecontrol.io/' + pf.replace(' ', '%20')).read(); dh = hashlib.sha256(by).hexdigest(); print('Error validating download (got %s instead of %s), please try manual install' % (dh, h)) if dh != h else open(os.path.join( ipp, pf), 'wb' ).write(by) 
```

2. Download <b>_Markdown Editing_</b> and <b>_Markdown Preview:Preview in Browser_</b> and 
<b>_LiveReload_</b>  
>  You can using <kbd>ctrl</kbd>+<kbd>shift</kbd>+<kbd>p</kbd>
>  and input <b>Package Control:Install Package</b> to open package install control,
>  wait seconds,input <b>_Markdown Editing_</b> and click to download the plugin.   
>
> Other plugins can download follow the up steps.  

3. Update configuration  
    - Open <i>Preferences – Package Settings – Markdown Preview – Setting</i>,
add ```enable_autoreload": true,``` in settings-user  
    - Enable LiveReload
open <kbd>ctrl</kbd>+<kbd>shift</kbd>+<kbd>p</kbd>, input <b>LiveReload: Enable/disable plug-ins</b>,
choose <b>Enable: Simple Reload</b>

4. Preference -->Key Binding-User  
set quick key <kbd>ctrl</kbd>+<kbd>m</kbd> to access preview in browser  
add following configuration
```
{ "keys": ["alt+m"], "command": "markdown_preview", "args": {"target": "browser", "parser":"markdown"} }
```