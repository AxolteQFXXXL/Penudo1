--=============Tabla Carro en Lua================

local Carro = {}
Carro.__index = Carro

--contructor
function Carro.new(modelo, marca, kilometraje, color)
    local self = setmetatable({}, Carro)
    self.modelo = modelo or "N/A"
    self.marca = marca or "N/A"
    self.kilometraje = kilometraje or 0
    self.color = color or "White"
    return self
end

--metodos de carro
function Carro:andar(kms)
    self.kilometraje = self.kilometraje + kms
end

-- el toString
function Carro:toString()
    mensaje = "Modelo: " ..self.modelo .. "\n Marca: " .. self.marca .. "\n Kilometraje:" .. self.kilometraje .. "\n Color: " .. self.color
    return mensaje .."\n"
end

return Carro
