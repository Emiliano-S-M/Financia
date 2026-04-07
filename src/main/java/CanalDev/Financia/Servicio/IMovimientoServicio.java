package CanalDev.Financia.Servicio;

import CanalDev.Financia.Modelo.MovimientoModelo;

import java.util.List;

public interface IMovimientoServicio {

    public List<MovimientoModelo> listarMovimientos();
    public MovimientoModelo guardarMovimiento(MovimientoModelo movimientoModelo);
    public boolean eliminarMovimiento(MovimientoModelo movimientoModelo);
    public boolean eliminarMovimientoPorId(Integer id);
}
