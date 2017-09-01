$declarations$

let $roles := ($roles$)

return(
for $item in $searchStatement$
return
  
  for $resource in /r:res[@r:id = string($item/@r:rsuiteId)]
where $roles = "RSuiteAdministrator" or $resource/r:acl/r:ace[contains(text(), "view")]/@r:role = $roles

return 
string($resource/@r:id)
)[1 to $limit$]