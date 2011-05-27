declare namespace mem="http://www.dbai.tuwien.ac.at/education/ssd/SS11/uebung/Memory";

declare function local:game() as element(mem:game)
{
	let $p := doc('memory.xml')/mem:game
	return $p
};

declare function local:players() as element(mem:player)*
{
	let $p := local:game()/mem:players/mem:player
	return $p
};

declare function local:cards() as element(mem:card)*
{
	let $p := local:game()/mem:table/mem:card
	return $p
};

declare function local:cardkeys_for_player($p as element(mem:player)) as xs:string*
{
	let $c := distinct-values($p/mem:uncovered-pairs/mem:pair/(@card1 | @card2))
	return $c
};

declare function local:cards_for_player($p as element(mem:player)) as element(mem:card)*
{
	for $card_key in local:cardkeys_for_player($p)
	let $card := local:cards()[@key eq $card_key][@column="1"]
	return $card
};

declare function local:cardcount_for_player($p as element(mem:player)) as xs:integer
{
	let $cards := local:cards_for_player($p)
	return count($cards)
};

declare function local:info_for_player($p as element(mem:player)) as element(player)
{
	let $q := $p
	return
	<player playing-time="{$p/@spent-time}">
	<name>{$p/@name}</name>
	<uncovered-count-column1>{local:cardcount_for_player($p)}</uncovered-count-column1>
	</player>

};

for $p in local:players()
let $info := local:info_for_player($p)
where $info/uncovered-count-column1 > 1
order by $info/uncovered-count-column1 descending, $info/name ascending
return $info


