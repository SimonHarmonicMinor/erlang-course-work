{application, bank_app,
[{description, "bank server"},
{vsn, "1.0"},
{modules, [bank_app, general_svisor,
 bank_server]}, 
 {registered, [ bank_server, general_svisor]},
 {applications, [kernel, stdlib]},
 {mod, {bank_app,[]}},
 {start_phases, []}]}.
