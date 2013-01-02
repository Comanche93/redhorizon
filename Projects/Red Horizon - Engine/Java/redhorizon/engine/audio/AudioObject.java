/*
 * Copyright 2013, Emanuel Rabina (http://www.ultraq.net.nz/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package redhorizon.engine.audio;

/**
 * Interface for audio objects 'rendered' to the audio hardware.
 * 
 * @author Emanuel Rabina
 */
public interface AudioObject {

	/**
	 * Delete the audio data associated with this object.
	 * 
	 * @param renderer Audio renderer for the underlying audio subsystem.
	 */
	public void delete(AudioRenderer renderer);

	/**
	 * Sets up the audio data associated with this object.
	 * 
	 * @param renderer Audio renderer for the underlying audio subsystem.
	 */
	public void init(AudioRenderer renderer);

	/**
	 * Plays any audio associated with this object.
	 * 
	 * @param renderer Audio renderer for the underlying audio subsystem.
	 */
	public void render(AudioRenderer renderer);
}
