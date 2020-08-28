
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

public final class Input {

  private final String text;
  private final double latitude;
  private final double longitude;

  public Input(String text, double latitude, double longitude) {
    this.text = text;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}