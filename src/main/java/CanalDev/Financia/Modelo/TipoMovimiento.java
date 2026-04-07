package CanalDev.Financia.Modelo;

public enum TipoMovimiento {

        INGRESO("Ingreso"),
        EGRESO("Egreso");

        private final String valor;

        TipoMovimiento(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

}
