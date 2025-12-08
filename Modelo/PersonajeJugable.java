package Modelo;

public class PersonajeJugable implements Personaje {
	private int hp;
	private int hpMaximo;
	private int defensa;
	private int defensaOriginal;
	private int defensaBase;
	private int ataque;
	private int ataqueBase;
	private int velocidad;
	private int velocidadBase;
	private int mp;
	private int mpMaximo;
	private Arma arma;
	private PoderEspecial poderEspecial;
	private String nombre;
	private TipoPersonaje tipoPersonaje;
	private Estado estado;
	private boolean estaDefendiendo;
	private java.util.ArrayList<Buff> buffsActivos;
	private EstadoEfecto estadoEfecto;

	public PersonajeJugable(TipoPersonaje tipoPersonaje, String nombre, int hp, int defensa, int ataque, int velocidad, Estado estado, Arma arma, int mp, PoderEspecial poderEspecial) {
		this.nombre = nombre;
		this.tipoPersonaje = tipoPersonaje;
		this.hpMaximo = Math.max(1, hp);
		this.hp = this.hpMaximo;
		this.ataque = ataque;
		this.ataqueBase = ataque;
		this.defensa = Math.max(0, defensa);
		this.defensaOriginal = this.defensa;
		this.defensaBase = this.defensa;
		this.velocidad = velocidad;
		this.velocidadBase = velocidad;
		this.estado = estado == null ? Estado.NORMAL : estado;
		this.poderEspecial = poderEspecial;
		this.arma = arma == null ? Arma.ESPADA : arma;
		this.mp = Math.max(0, mp);
		this.mpMaximo = Math.max(0, mp);
		this.estaDefendiendo = false;
		this.buffsActivos = new java.util.ArrayList<>();
		this.estadoEfecto = null;
	}

	@Override
	public int getHP() { return hp; }

	@Override
	public void setHP(int hp) {
		this.hp = Math.max(0, Math.min(hp, this.hpMaximo));
		if (this.hp == 0) this.estado = Estado.MUERTO;
	}

	@Override
	public int getDefensa() { return defensa; }

	@Override
	public void setDefensa(int defensa) { this.defensa = Math.max(0, defensa); }

	@Override
	public int getAtaque() { return ataque; }

	@Override
	public void setAtaque(int ataque) { this.ataque = Math.max(0, ataque); }

	@Override
	public int getVelocidad() { return velocidad; }

	@Override
	public void setVelocidad(int velocidad) { this.velocidad = Math.max(0, velocidad); }

	public int getMP() { return mp; }
	public void setMP(int mp) { this.mp = Math.max(0, Math.min(mp, this.mpMaximo)); }

	@Override
	public Estado getEstado() { return estado; }

	@Override
	public void setEstado(Estado estado) {
		
		this.estado = Estado.NORMAL;
		this.estadoEfecto = null;
	}

	public void setEstadoConDuracion(Estado estado, int turnos) {
		
		this.estado = Estado.NORMAL;
		this.estadoEfecto = null;
	}

	public void setEstadoConDebilitacion(int valorDebilitacion, int turnos) {
		
		this.estado = Estado.NORMAL;
		this.estadoEfecto = null;
	}

	public EstadoEfecto getEstadoEfecto() { return estadoEfecto; }

	public void setPoderEspecial(PoderEspecial poderEspecial) { this.poderEspecial = poderEspecial; }
	public PoderEspecial getPoderEspecial() { return poderEspecial; }

	@Override
	public String getNombre() { return nombre; }

	@Override
	public void setNombre(String nombre) { this.nombre = nombre; }

	@Override
	public TipoPersonaje getTipoPersonaje() { return tipoPersonaje; }

	@Override
	public void SetTipoPersonaje(TipoPersonaje tipoPersonaje) { this.tipoPersonaje = tipoPersonaje; }

	@Override
	public void tomarTurno(Personaje objetivo, int opcion) {
		if (hp <= 0 || estado == Estado.MUERTO) return;

		
		actualizarBuffs();

		if (estaDefendiendo) {
			this.defensa = defensaOriginal;
			estaDefendiendo = false;
		}

		if (opcion == 1) atacar(objetivo);
		else if (opcion == 2) usarPoderEspecial(objetivo);
		else if (opcion == 3) defender();
	}

	@Override
	public void atacar(Personaje objetivo) {
		if (objetivo == null || objetivo.getEstado() == Estado.MUERTO) return;

		int ataqueFinal;
		int danio;
		switch (arma) {
			case ESPADA:
				ataqueFinal = (int)(this.ataque * 1.10);
				danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
				objetivo.setHP(objetivo.getHP() - danio);
				break;
			case HACHA:
				ataqueFinal = (int)(this.ataque * 1.30);
				danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
				objetivo.setHP(objetivo.getHP() - danio);
				this.velocidad = Math.max(this.velocidad - 10, 0);
				objetivo.setDefensa(Math.max(objetivo.getDefensa() - 10, 0));
				break;
			case BASTON:
				ataqueFinal = this.ataque;
				danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
				objetivo.setHP(objetivo.getHP() - danio);
				this.mp = Math.min(this.mp + 20, this.mpMaximo);
				this.defensa = Math.max(this.defensa - 5, 0);
				this.defensaOriginal = this.defensa;
				break;
			case ARCO:
				ataqueFinal = this.ataque;
				danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
				objetivo.setHP(objetivo.getHP() - danio);
				this.velocidad += 5;
				objetivo.setVelocidad(Math.max(objetivo.getVelocidad() - 5, 0));
				break;
			default:
				ataqueFinal = this.ataque;
				danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
				objetivo.setHP(objetivo.getHP() - danio);
				break;
		}
	}

	public void usarPoderEspecial(Personaje objetivo) {
		if (this.hp <= 0) return;
		if (this.mp < 50) return;
		setMP(this.mp - 50);

		switch (this.poderEspecial) {
			case ENVENENAR:
				// Do not apply status effects to playable characters
				if (objetivo instanceof Monstruo) {
					((Monstruo) objetivo).setEstadoConDuracion(Estado.ENVENENADO, 3);
				} else if (objetivo != null && !(objetivo instanceof PersonajeJugable)) {
					objetivo.setEstado(Estado.ENVENENADO);
				}
				break;

			case ATURDIR:
				if (objetivo instanceof Monstruo) {
					((Monstruo) objetivo).setEstadoConDuracion(Estado.ATURDIDO, 1);
				} else if (objetivo != null && !(objetivo instanceof PersonajeJugable)) {
					objetivo.setEstado(Estado.ATURDIDO);
				}
				break;

			case DEBILITAR:
				if (objetivo != null && !(objetivo instanceof PersonajeJugable) && objetivo.getEstado() != Estado.DEBILITADO) {
					int reduccionAtaque = 10;
					objetivo.setAtaque(Math.max(objetivo.getAtaque() - reduccionAtaque, 0));
					if (objetivo instanceof Monstruo) {
						((Monstruo) objetivo).setEstadoConDebilitacion(reduccionAtaque, 3);
					} else {
						objetivo.setEstado(Estado.DEBILITADO);
					}
				}
				break;

			case CONGELAR:
				if (objetivo instanceof Monstruo) {
					((Monstruo) objetivo).setEstadoConDuracion(Estado.CONGELADO, 1);
				} else if (objetivo != null && !(objetivo instanceof PersonajeJugable)) {
					objetivo.setEstado(Estado.CONGELADO);
				}
				break;
		}
	}

	@Override
	public void defender() {
		this.defensaOriginal = this.defensa;
		this.defensa = (int)(this.defensa * 1.5);
		this.estaDefendiendo = true;
	}

	@Override
	public void usarItem(Item item, Personaje objetivo) {
		if (item == null || objetivo == null) return;

		switch (item.getTipo()) {
			case CURACION: {
				int nuevoHP = Math.min(objetivo.getHP() + item.getValor(), objetivo.getHPMaximo());
				objetivo.setHP(nuevoHP);
				break;
			}
			case RECUPERACION_MP: {
				if (objetivo instanceof PersonajeJugable) {
					PersonajeJugable pj = (PersonajeJugable) objetivo;
					int nuevoMP = Math.min(pj.getMP() + item.getValor(), pj.getMPMaximo());
					pj.setMP(nuevoMP);
				}
				break;
			}
			case CURACION_ESTADO: {
				Estado estadoAnterior = objetivo.getEstado();
				switch (item) {
					case ANTIDOTO:
						if (estadoAnterior == Estado.ENVENENADO) objetivo.setEstado(Estado.NORMAL);
						break;
					case ESTIMULANTE:
						if (estadoAnterior == Estado.ATURDIDO || estadoAnterior == Estado.CONGELADO) objetivo.setEstado(Estado.NORMAL);
						break;
					case PANACEA:
						objetivo.setEstado(Estado.NORMAL);
						break;
					default: break;
				}
				break;
			}
			case BUFF: {
				if (objetivo instanceof PersonajeJugable) {
					PersonajeJugable pj = (PersonajeJugable) objetivo;
					switch (item) {
						case POCION_FUERZA:
							pj.agregarBuff(new Buff(TipoBuff.ATAQUE, 20, 3));
							break;
						case POCION_DEFENSA:
							pj.agregarBuff(new Buff(TipoBuff.DEFENSA, 20, 3));
							break;
						case POCION_VELOCIDAD:
							pj.agregarBuff(new Buff(TipoBuff.VELOCIDAD, 15, 3));
							break;
						default: break;
					}
				}
				break;
			}
			default: break;
		}
	}

	@Override
	public int getHPMaximo() { return hpMaximo; }

	@Override
	public int getMPMaximo() { return mpMaximo; }

	public void agregarBuff(Buff buff) {
		if (buff == null) return;
		buffsActivos.add(buff);
		aplicarBuffs();
	}

	public void aplicarBuffs() {
		this.ataque = this.ataqueBase;
		this.defensa = this.defensaBase;
		this.velocidad = this.velocidadBase;
		for (Buff b : buffsActivos) {
			switch (b.getTipo()) {
				case ATAQUE: this.ataque += b.getValor(); break;
				case DEFENSA: this.defensa += b.getValor(); break;
				case VELOCIDAD: this.velocidad += b.getValor(); break;
				default: break;
			}
		}
		if (!estaDefendiendo) this.defensaOriginal = this.defensa;
	}

	public void actualizarBuffs() {
		java.util.ArrayList<Buff> expirados = new java.util.ArrayList<>();
		for (Buff b : buffsActivos) {
			b.reducirTurno();
			if (b.haExpirado()) expirados.add(b);
		}
		buffsActivos.removeAll(expirados);
		aplicarBuffs();
	}

	public void actualizarEstados() {
		if (estadoEfecto != null) {
			estadoEfecto.reducirTurno();
			if (estadoEfecto.haExpirado()) {
				Estado prev = estadoEfecto.getTipo();
				if (prev == Estado.DEBILITADO) {
					this.ataque += estadoEfecto.getValorDebilitacion();
					this.ataqueBase += estadoEfecto.getValorDebilitacion();
				}
				this.estado = Estado.NORMAL;
				this.estadoEfecto = null;
			}
		}
	}
}
