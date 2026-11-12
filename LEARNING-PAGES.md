# 专业页面命名与跳转

静态页面采用全小写、短横线分隔。类别使用 category，专业使用 major，路线使用 route，资源使用 resource。

| 旧地址（兼容） | 新地址 |
| --- | --- |
| `moreMajors.html` | `major-categories.html` |
| `learnMajorCategory.html` | `category-detail.html` |
| `learncomputerindex.html` | `category-computer.html` |
| `learnmechanicalindex.html` | `category-mechanical.html` |
| `learnautomationindex.html` | `category-automation.html` |
| `learncomputer.html` | `route-computer-employment.html` |
| `learnmechanical.html` | `route-mechanical-mechatronics.html` |
| `learnautomation.html` | `route-automation.html` |
| `learnXXX.html` | `route-template.html` |
| `majorDemo.html` | `major-detail.html` |
| `routeDemo.html` | `route-detail.html` |
| `resourceDemo.html` | `resource-placeholder.html` |
| `resourceDetail.html` | `resource-detail.html` |
| `linkResource.html` | `resource-library.html` |

新增独立专业可命名为 major-software-engineering.html，新增路线可命名为 route-computer-algorithms.html；本次未创建这些示例内容页。

类别总览 → 类别专用入口 → category-detail.html?category=computer。分类详情继续从现有接口获取目录，mode=major 默认显示具体专业；实际路线页可通过独立 HTML 自由排版。

resource-detail.html 是现有接口驱动的资源详情，resource-placeholder.html 是通用资源占位页，两者用途不同。

旧地址为薄跳转页，使用 location.replace，保留查询参数和锚点，避免污染后退历史。数据库中 target_url 无需更新。后端权限仍通过现有接口和会话检查生效。旧 HTML 文件暂勿删除。关闭 JavaScript 时可点击页面链接继续，但不会自动合并查询参数。
