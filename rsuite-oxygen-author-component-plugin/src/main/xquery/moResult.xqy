
declare function local:transform($childMap as map:map, $parentMap as map:map, $nodes as node()*) as node()*
{
for $n in $nodes//*[@r:rsuiteId] return
local:processElement($childMap, $parentMap, $n)
};

declare function local:processElement($childMap as map:map, $parentMap as map:map, $n as node()) as node()*{

let $anscestors := $n/ancestor::*[@r:rsuiteId]/@r:rsuiteId
let $temp := map:put($parentMap, $n/@r:rsuiteId,
$anscestors)
for $ancestorId in $anscestors
return 
map:put($childMap, $ancestorId,
"true")

};


declare function local:resourceUris($resources as node()*) as xs:string*{
let $uris := for $x in $resources   
return
$x/r:vt/r:v/r:uri

return
distinct-values($uris)
};

declare function local:createRelationshipMap($resources as node()*) as map:map*{

let $childMap := map:map()
let $parentMap := map:map()

let $uris := local:resourceUris($resources)

let $result :=  for $uri in $uris
return 
local:transform($childMap, $parentMap, doc($uri))

return ($childMap, $parentMap)

};

let $resourceIDs := ($resourceIDs$)
let $resources :=  /r:res[@r:id = $resourceIDs]

 let $maps := local:createRelationshipMap($resources)
let $childMap := $maps[1]
let $parentMap := $maps[2] 

(: let $childMap := map:map()
let $parentMap := map:map() :)

for $item in $resources
let $rsuiteId := $item/@r:id 
return
<item id="{$rsuiteId}"
    hasChildren="{map:get($childMap, $rsuiteId)}" 
    ancestorIds="{map:get($parentMap, $rsuiteId)}"
    
    
    >
    {$item}
</item>