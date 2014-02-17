package nl.q42.jue;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import nl.q42.jue.State.AlertMode;
import nl.q42.jue.State.Effect;

/**
 * Collection of updates to the state of a light.
 */
@JsonSerialize(include=Inclusion.NON_NULL)
public class StateUpdate {
	private Boolean on;
	private Integer bri;
	private Integer hue;
	private Integer sat;
	private Integer ct;
	private Integer transitionTime;
	private float[] XY;
	private AlertMode alert;
	private Effect effect;

	private ArrayList<Command> commands = new ArrayList<Command>();
	
	String toJson() {
		StringBuilder json = new StringBuilder("{");
		
		for (int i = 0; i < commands.size(); i++) {
			json.append(commands.get(i).toJson());
			if (i < commands.size() - 1) json.append(",");
		}
		
		json.append("}");
		
		return json.toString();
	}
	
	/**
	 * Turn light on.
	 * @return this object for chaining calls
	 */
	public StateUpdate turnOn() {
		return setOn(true);
	}
	
	/**
	 * Turn light off.
	 * @return this object for chaining calls
	 */
	public StateUpdate turnOff() {
		return setOn(false);
	}
	
	/**
	 * Turn light on or off.
	 * @param on on if true, off otherwise
	 * @return this object for chaining calls
	 */
	public StateUpdate setOn(boolean on) {
		commands.add(new Command("on", on));
		this.on = on;
		return this;
	}
	
	/**
	 * Set brightness of light.
	 * Brightness 0 is not the same as off.
	 * @param brightness brightness [1..255]
	 * @return this object for chaining calls
	 */
	@JsonProperty("bri")
	public StateUpdate setBrightness(Integer brightness) {
		if (brightness < 0 || brightness > 255) {
			throw new IllegalArgumentException("Brightness out of range");
		}
		
		commands.add(new Command("bri", brightness));
		this.bri = brightness;
		return this;
	}
	
	/**
	 * Switch to HS color mode and set hue.
	 * @param hue hue [0..65535]
	 * @return this object for chaining calls
	 */
	public StateUpdate setHue(Integer hue) {
		if (hue < 0 || hue > 65535) {
			throw new IllegalArgumentException("Hue out of range");
		}
		
		commands.add(new Command("hue", hue));
		this.hue = hue;
		return this;
	}
	
	/**
	 * Switch to HS color mode and set saturation.
	 * @param saturation saturation [0..255]
	 * @return this object for chaining calls
	 */
	public StateUpdate setSat(Integer saturation) {
		if (saturation < 0 || saturation > 255) {
			throw new IllegalArgumentException("Saturation out of range");
		}
		
		commands.add(new Command("sat", saturation));
		this.sat = saturation;
		return this;
	}
	
	/**
	 * Switch to XY color mode and set CIE color space coordinates.
	 * @param x x coordinate [0..1]
	 * @param y y coordinate [0..1]
	 * @return this object for chaining calls
	 */
	public StateUpdate setXY(float x, float y) {
		return setXY(new float[] {x, y});
	}
	
	/**
	 * Switch to XY color mode and set CIE color space coordinates.
	 * @param xy x and y coordinates [0..1, 0..1]
	 * @return this object for chaining calls
	 */
	public StateUpdate setXY(float[] xy) {
		if (xy.length != 2) {
			throw new IllegalArgumentException("Invalid coordinate array given");
		} else if (xy[0] < 0.0f || xy[0] > 1.0f || xy[1] < 0.0f || xy[1] > 1.0f) {
			throw new IllegalArgumentException("X and/or Y coordinate(s) out of bounds");
		}
		
		commands.add(new Command("xy", xy));
		this.XY = xy;
		return this;
	}
	
	/**
	 * Switch to CT color mode and set color temperature in mired.
	 * @param colorTemperature color temperature [153..500]
	 * @return this object for chaining calls
	 */
	@JsonProperty("ct")
	public StateUpdate setColorTemperature(Integer colorTemperature) {
		if (colorTemperature < 153 || colorTemperature > 500) {
			throw new IllegalArgumentException("Color temperature out of range");
		}
		
		commands.add(new Command("ct", colorTemperature));
		this.ct = colorTemperature;
		return this;
	}
	
	/**
	 * Set the alert mode.
	 * @see AlertMode
	 * @param mode alert mode
	 * @return this object for chaining calls
	 */
	public StateUpdate setAlert(AlertMode mode) {
		commands.add(new Command("alert", mode.toString().toLowerCase()));
		this.alert = mode;
		return this;
	}
	
	/**
	 * Set the current effect.
	 * @see Effect
	 * @param effect effect 
	 * @return this object for chaining calls
	 */
	public StateUpdate setEffect(Effect effect) {
		commands.add(new Command("effect", effect.toString().toLowerCase()));
		this.effect = effect;
		return this;
	}
	
	/**
	 * Set the transition time from the current state to the new state.
	 * Time is accurate to 100 milliseconds.
	 * @param timeMillis time in milliseconds [0..6553600]
	 * @return this object for chaining calls
	 */
	public StateUpdate setTransitionTime(Integer timeMillis) {
		if (timeMillis < 0 || timeMillis > 6553600) {
			throw new IllegalArgumentException("Transition time out of range");
		}
		
		commands.add(new Command("transitiontime", timeMillis / 100));
		this.transitionTime = timeMillis;
		return this;
	}

	public Integer getBri() {
		return bri;
	}

	public Integer getCt() {
		return ct;
	}

	public float[] getXY() {
		return XY;
	}

	public Boolean getOn() {
		return on;
	}

	public Integer getHue() {
		return hue;
	}

	public Integer getSat() {
		return sat;
	}

	public Integer getTransitionTime() {
		return transitionTime;
	}

	public AlertMode getAlert() {
		return alert;
	}

	public Effect getEffect() {
		return effect;
	}
}
