local Carro = require("Carro")

local Axioma = Carro.new("FAXXOL", "Tesla",0, "Rosa")
print(Axioma:toString())
Axioma:andar(300)
print(Axioma:toString())
