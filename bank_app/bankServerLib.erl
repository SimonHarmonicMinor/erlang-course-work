-module(bankServerLib).
-export([init/0, check_balance/1, put_money/2, transfer_money/3, withdraw_money/2]).

init() ->
    ets:new(store, [set, named_table]).

check_balance(Id) ->
    check_exist_user(Id).

check_exist_user(Id) ->
    check_exist_user(ets:lookup(store, Id), Id).

check_exist_user([{_Id, Money}], _Id) ->
    Money;
check_exist_user([], Id) -> 
    ets:insert(store, {Id, 0}),
    0.

put_money(Id, Money) ->
    ets:insert(store, {Id, check_balance(Id) + Money}),
    'Done'.

transfer_money(Id_Src, Id_Dest, Money) ->
    Balance = check_balance(Id_Src),
    if Balance < Money ->
	    'Not enough money in the account';
       true ->
	    ets:insert(store, {Id_Src, check_balance(Id_Src) - Money}),
	    ets:insert(store, {Id_Dest, check_balance(Id_Dest) + Money}),
	    'Done'
    end.

withdraw_money(Id, Money) ->
    Balance = check_balance(Id),
    if Balance < Money ->
	    'Not enough money in the account';
       true ->
	    ets:insert(store, {Id, check_balance(Id) - Money}),
	    'Done'
    end.
