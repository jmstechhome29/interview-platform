{{- define "common.configmap" -}}
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Chart.Name }}-config
data:
  {{- toYaml .Values.config | nindent 2 }}
{{- end -}}
