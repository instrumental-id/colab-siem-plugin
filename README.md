[![Discourse Topics][discourse-shield]][discourse-url]
[![Issues][issues-shield]][issues-url]
[![Latest Releases][release-shield]][release-url]
[![Contributor Shield][contributor-shield]][contributors-url]

[discourse-shield]:https://img.shields.io/discourse/topics?label=Discuss%20This%20Tool&server=https%3A%2F%2Fdeveloper.sailpoint.com%2Fdiscuss
[discourse-url]:https://developer.sailpoint.com/discuss/t/sailpoint-siem-plugin/14268
[issues-shield]:https://img.shields.io/github/issues/sailpoint-oss/colab-siem-plugin?label=Issues
[issues-url]:https://github.com/sailpoint-oss/colab-siem-plugin/issues
[release-shield]: https://img.shields.io/github/v/release/sailpoint-oss/colab-siem-plugin?label=Current%20Release
[release-url]:https://github.com/sailpoint-oss/colab-siem-plugin/releases
[contributor-shield]:https://img.shields.io/github/contributors/sailpoint-oss/colab-siem-plugin?label=Contributors
[contributors-url]:https://github.com/sailpoint-oss/colab-siem-plugin/graphs/contributors

# SailPoint SIEM Plugin
[Explore the docs »](https://developer.sailpoint.com/discuss/t/sailpoint-siem-plugin/14268)

[New to the CoLab? Click here »](https://developer.sailpoint.com/discuss/t/about-the-sailpoint-developer-community-colab/11230)

## Building

This plugin requires IdentityIQ 8.4 or later. It builds with Gradle (use the included wrapper). You need JDK 11 or later and an exploded IdentityIQ 8.4+ install to compile against, meaning the folder that contains `WEB-INF`.

```bash
./gradlew -PiiqHome=/path/to/identityiq pluginZip
```

To avoid passing the path every time, put `iiqHome=/path/to/identityiq` in `~/.gradle/gradle.properties`. The zip to install in IdentityIQ is written to `build/dist/`.

The plugin version is set at build time to `yyyy.MMdd.HHmmss.v<git short hash>`. `manifest.xml` holds the `%%BUILD_VERSION%%` placeholder, and the build replaces it in the zip only.

`./gradlew -PiiqHome=... test` runs the unit tests.

## Limiting audit events

The `SIEMPlugin/audit-events` endpoint returns audit events from the last 24 hours unless `startTime` and `endTime` are given. Not all audit events are suitable for reporting. They may include private data or a lot of noise, for example.

Plugin settings (administrator policy, applies to every caller):

- **Audit actions to include** (`auditEventIncludeActions`): comma separated actions to return. Blank includes all.
- **Audit actions to exclude** (`auditEventExcludeActions`): comma separated actions to leave out.

Query parameters (a caller can narrow the results further, never widen them):

- `type`: comma separated actions to return. A single value works as before.
- `excludeType`: comma separated actions to leave out.

A trailing `*` matches a prefix, so `historyDiff*` covers every action that starts with `historyDiff`. An event is returned only if it passes the settings and the query parameters. Filtering happens in the database query, so `totalResults` and paging reflect the filtered set.

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag `enhancement`.
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<!-- CONTACT -->
## Discuss
[Click Here](https://developer.sailpoint.com/discuss/new-topic?title=Your%20CoLab%20question%20title&body=Your%20CoLab%20question%20body%20here&category_id=2&tags=colab) to discuss the Colab with other users.