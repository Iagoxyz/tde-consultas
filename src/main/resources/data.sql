insert into tb_user (email, nome, senha) values
('joao.silva@email.com', 'João Silva', '123456'),
('maria.souza@email.com', 'Maria Souza', 'abcdef');

-- Inserindo pacientes
insert into tb_paciente (
    data_nascimento, bairro, cep, cidade, estado, numero, rua,
    paciente_cpf, paciente_email, paciente_nome, paciente_telefone
) values
('1990-05-10', 'Centro', '40000000', 'Salvador', 'BA', '123', 'Rua das Flores',
 '11111111111', 'paciente1@email.com', 'Carlos Pereira', '71999990000'),
('1985-07-22', 'Pituba', '40150000', 'Salvador', 'BA', '456', 'Av. Oceânica',
 '22222222222', 'paciente2@email.com', 'Ana Lima', '71988887777');

-- Inserindo procedimentos
insert into tb_procedimento (descricao, nome, valor_particular, valor_plan) values
('Consulta médica geral', 'Consulta', 200.00, 100.00),
('Exame de sangue completo', 'Exame de sangue', 150.00, 80.00),
('Raio-X do tórax', 'Raio-X', 250.00, 120.00);

-- Inserindo atendimentos
insert into tb_atendimento (date_time, plan_number, tipo, valor_total, paciente_paciente_id, usuario_id) values
('2025-09-16 10:00:00', 'PL12345', 'PLANO', 180.00, 1, 1),
('2025-09-16 11:30:00', null, 'PARTICULAR', 200.00, 2, 2);

-- Relacionando atendimentos e procedimentos (tabela N-N)
insert into atendimento_procedimento (atendimento_id, procedimento_id) values
(1, 1), -- Atendimento 1 fez Consulta
(1, 2), -- Atendimento 1 fez Exame de sangue
(2, 1); -- Atendimento 2 fez apenas Consulta