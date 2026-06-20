{{- define "common.secret" -}}
{{- if .Values.secretEnabled }}
apiVersion: v1
kind: Secret
metadata:
  name: {{ .Chart.Name }}-secret
type: Opaque
stringData:
  {{- toYaml .Values.secrets | nindent 2 }}
{{- end }}
{{- end -}}
