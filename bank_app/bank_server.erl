%%%-------------------------------------------------------------------
%%% @author tester <tester@testerPC>
%%% @copyright (C) 2019, tester
%%% @doc
%%%
%%% @end
%%% Created :  3 Dec 2019 by tester <tester@testerPC>
%%%-------------------------------------------------------------------
-module(bank_server).

-behaviour(gen_server).

%% API
-export([start_link/0]).

%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
	 terminate/2, code_change/3]).

-define(SERVER, ?MODULE).

-record(state, {}).

%%%===================================================================
%%% API
%%%===================================================================

%%--------------------------------------------------------------------
%% @doc
%% Starts the server
%%
%% @spec start_link() -> {ok, Pid} | ignore | {error, Error}
%% @end
%%--------------------------------------------------------------------
start_link() ->
    gen_server:start_link({local, ?SERVER}, ?MODULE, [], []).

%%%===================================================================
%%% gen_server callbacks
%%%===================================================================

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Initializes the server
%%
%% @spec init(Args) -> {ok, State} |
%%                     {ok, State, Timeout} |
%%                     ignore |
%%                     {stop, Reason}
%% @end
%%--------------------------------------------------------------------
init([]) ->
    process_flag(trap_exit, true),
    bankServerLib:init(),
    {ok, #state{}}.

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Handling call messages
%%
%% @spec handle_call(Request, From, State) ->
%%                                   {reply, Reply, State} |
%%                                   {reply, Reply, State, Timeout} |
%%                                   {noreply, State} |
%%                                   {noreply, State, Timeout} |
%%                                   {stop, Reason, Reply, State} |
%%                                   {stop, Reason, State}
%% @end
%%--------------------------------------------------------------------
handle_call({'CHECK_BALANCE', Id}, _From, State) ->
    %gen_event:notify(logger, {msg, "CALL CHECK_BALANCE Id: ~p", [Id]}),
    Reply = bankServerLib:check_balance(Id),
    {reply, Reply, State};
handle_call({'PUT_MONEY', Id, Money}, _From, State) ->
    %gen_event:notify(logger, {msg, "CALL PUT_MONEY Id: ~p Money: ~p", [Id, Money]}),
    Reply = bankServerLib:put_money(Id, Money),
    {reply, Reply, State};
handle_call({'TRANSFER_MONEY', Id_Src, Id_Dest, Money}, _From, State) ->
    %gen_event:notify(logger, {msg, "CALL TRANSFER_MONEY Id source: ~p Id dest: ~p Money: ~p", [Id_Src, Id_Dest, Money]}),
    Reply = bankServerLib:transfer_money(Id_Src, Id_Dest, Money),
    {reply, Reply, State};
handle_call({'WITHDRAW_MONEY', Id, Money}, _From, State) ->
    %gen_event:notify(logger, {msg, "CALL WITHDRAW_MONEY Id: ~p Money: ~p", [Id, Money]}),
    Reply = bankServerLib:withdraw_money(Id, Money),
    {reply, Reply, State};
handle_call(_Request, _From, State) ->
    Reply = default,
    {reply, Reply, State}.

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Handling cast messages
%%
%% @spec handle_cast(Msg, State) -> {noreply, State} |
%%                                  {noreply, State, Timeout} |
%%                                  {stop, Reason, State}
%% @end
%%--------------------------------------------------------------------
handle_cast({'CHECK_BALANCE', Id, From}, State) ->
    Reply = bankServerLib:check_balance(Id),
    gen_server:cast(From, Reply),
    {noreply, State};
handle_cast({'PUT_MONEY', Id, Money, From}, State) ->
    Reply = bankServerLib:put_money(Id, Money),
    gen_server:cast(From, Reply),
    {noreply, State};
handle_cast({'TRANSFER_MONEY', Id_Src, Id_Dest, Money, From}, State) ->
    Reply = bankServerLib:transfer_money(Id_Src, Id_Dest, Money),
    gen_server:cast(From, Reply),
    {noreply, State};
handle_cast({'WITHDRAW_MONEY', Id, Money, From}, State) ->
    Reply = bankServerLib:withdraw_money(Id, Money),
    gen_server:cast(From, Reply),
    {noreply, State};
handle_cast(_Msg, State) ->
    {noreply, State}.

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Handling all non call/cast messages
%%
%% @spec handle_info(Info, State) -> {noreply, State} |
%%                                   {noreply, State, Timeout} |
%%                                   {stop, Reason, State}
%% @end
%%--------------------------------------------------------------------
handle_info(_Info, State) ->
    {noreply, State}.

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called by a gen_server when it is about to
%% terminate. It should be the opposite of Module:init/1 and do any
%% necessary cleaning up. When it returns, the gen_server terminates
%% with Reason. The return value is ignored.
%%
%% @spec terminate(Reason, State) -> void()
%% @end
%%--------------------------------------------------------------------
terminate(_Reason, _State) ->
    ok.

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Convert process state when code is changed
%%
%% @spec code_change(OldVsn, State, Extra) -> {ok, NewState}
%% @end
%%--------------------------------------------------------------------
code_change(_OldVsn, State, _Extra) ->
    {ok, State}.

%%%===================================================================
%%% Internal functions
%%%===================================================================
